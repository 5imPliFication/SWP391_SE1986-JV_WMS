package com.example.service;

import com.example.dao.*;
import com.example.dto.ExportDTO;
import com.example.dto.OrderDTO;
import com.example.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductItemDAO productItemDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final StockMovementDAO stockMovementDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        productItemDAO = new ProductItemDAO();
        productDAO = new ProductDAO();
        userDAO = new UserDAO();
        stockMovementDAO = new StockMovementDAO();
    }

    public Map<String, Integer> getOrderStatistics() {
        return orderDAO.getStatistics();
    }

    /* ================= SALESMAN ================= */

    public int createDraftOrder(String customerName, String customerPhone, String note, Long salesmanId) {
        Order order = new Order();
        order.setOrderCode("ORD-" + UUID.randomUUID());
        order.setCustomerName(customerName);
        order.setCustomerPhone(customerPhone);
        order.setStatus("DRAFT");
        order.setNote(note);
        order.setCreatedBy(userDAO.findUserById(salesmanId));
        System.out.println("Service layer ----------");
        System.out.println(order.getOrderCode() + " ++ " + order.getStatus() + " ++ " + order.getCreatedBy());
        return orderDAO.create(order);
    }

    public void removeItem(Long orderId, Long orderItemId) {
        Order order = orderDAO.findById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if (!"DRAFT".equals(order.getStatus()))
            throw new IllegalStateException("Order is not editable");

        OrderItem orderItem = orderItemDAO.findByIdAndOrderId(orderId, orderItemId);
        if (orderItem == null)
            throw new IllegalArgumentException("Item not found");

        orderItemDAO.deleteByOrderItemId(orderId, orderItemId);
        orderDAO.refreshOrderFinalTotal(orderId);
    }

    public void refreshOrderFinalTotal(Long orderId) {
        orderDAO.refreshOrderFinalTotal(orderId);
    }

    /* ================= SHARED ================= */

    public Order getOrderDetail(Long orderId, Long userId, String role) {
        Order order = orderDAO.findById(orderId);
        if (order == null) return null;

        if ("Salesman".equals(role) && !Objects.equals(order.getCreatedBy().getId(), userId))
            throw new SecurityException("Access denied");

        // Load creator user
        if (order.getCreatedBy() != null) {
            userDAO.findUserById(order.getCreatedBy().getId());
        }

        return order;
    }

    // Load creator info for a single order (when needed)
    public void loadCreator(Order order) {
        if (order.getCreatedBy() != null) {
            userDAO.findUserById(order.getCreatedBy().getId());
        }
    }

    // Batch load creators for multiple orders (more efficient)
    public void loadCreatorsForOrders(List<Order> orders) {
        orders.forEach(this::loadCreator);
    }

    /* ================= WAREHOUSE ================= */

    public List<Order> getSubmittedOrders() {
        return orderDAO.findByStatus("SUBMITTED");
    }

    /**
     * Start processing order and update inventory
     * This is when stock quantity actually decreases
     */
    public void startProcessing(Long orderId, Long warehouseKeeperId) {
        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("SUBMITTED"))
            throw new IllegalStateException("Order is not in queue");

        // Get order items
        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
        List<Long> deactivatedItemIds = new ArrayList<>();

        try {
            // Reduce stock for each linked product item
            for (OrderItem item : items) {
                List<ProductItem> linkedItems = item.getProductItems();
                if (linkedItems == null || linkedItems.isEmpty()) {
                    throw new IllegalStateException("No linked product items found for order item ID: " + item.getId());
                }

                if (linkedItems.size() < item.getQuantity()) {
                    throw new IllegalStateException("Linked item count does not match quantity for order item ID: " + item.getId());
                }

                // Decrease exactly the ordered quantity, even if more linked items are present.
                List<Long> linkedIds = linkedItems.stream()
                        .limit(item.getQuantity())
                        .map(ProductItem::getId)
                        .toList();

                int deactivated = productItemDAO.deactivateItemsByIds(linkedIds);
                if (deactivated < linkedIds.size()) {
                    String productName = item.getProduct() != null ? item.getProduct().getName() : ("order item " + item.getId());
                    throw new IllegalStateException("Insufficient active stock for " + productName);
                }

                deactivatedItemIds.addAll(linkedIds);
            }

            // Update order status
            boolean updated = orderDAO.updateStatus(orderId, "PROCESSING", warehouseKeeperId, null);
            if (!updated) {
                throw new IllegalStateException("Failed to update order status to PROCESSING");
            }
        } catch (RuntimeException e) {
            if (!deactivatedItemIds.isEmpty()) {
                try {
                    productItemDAO.activateItemsByIds(deactivatedItemIds);
                } catch (RuntimeException rollbackEx) {
                    throw new IllegalStateException("Failed during processing and failed to rollback stock changes", rollbackEx);
                }
            }
            throw e;
        }
    }

    public void completeProcessing(Long orderId, Long warehouseKeeperId) {
        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("PROCESSING"))
            throw new IllegalStateException("Order is not in queue");

        orderDAO.updateStatus(orderId, "COMPLETED", warehouseKeeperId, null);

        // Record stock movement for EXPORT
        List<com.example.dto.OrderItemDTO> orderItems = orderItemDAO.findOrderItemsByOrderId(orderId);
        Map<Long, Long> quantityByProduct = new HashMap<>();
        for (com.example.dto.OrderItemDTO itemDTO : orderItems) {
            quantityByProduct.put(itemDTO.getProductId(), (long) itemDTO.getQuantity());
        }

        if (!quantityByProduct.isEmpty()) {
            stockMovementDAO.insertStockMovements(
                    quantityByProduct,
                    com.example.enums.MovementType.EXPORT,
                    com.example.enums.ReferenceType.ORDER,
                    orderId
            );
        }
    }


    public void cancelOrder(Long orderId, Long userId) {
        cancelOrder(orderId, userId, null);
    }

    public void cancelOrder(Long orderId, Long userId, String note) {
        Order order = orderDAO.findById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        User user = userDAO.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        String userRole = user.getRole().getName();

        if ("Salesman".equalsIgnoreCase(userRole)) {
            // Salesman can only cancel their own draft orders
            if (!order.getCreatedBy().getId().equals(userId)) {
                throw new SecurityException("You can only cancel your own orders");
            }
            if (!"DRAFT".equals(order.getStatus())) {
                throw new IllegalStateException("You can only cancel draft orders. This order is " + order.getStatus());
            }
        } else if ("Warehouse".equalsIgnoreCase(userRole)) {
            // Warehouse can cancel SUBMITTED or PROCESSING orders
            if (!"SUBMITTED".equals(order.getStatus()) && !"PROCESSING".equals(order.getStatus())) {
                throw new IllegalStateException("Can only cancel SUBMITTED or PROCESSING orders. This order is " + order.getStatus());
            }
            // Note is required for warehouse cancellations
            if (note == null || note.trim().isEmpty()) {
                throw new IllegalArgumentException("Cancellation reason is required");
            }

            // If order is PROCESSING, restore inventory by linked product item IDs
            if ("PROCESSING".equals(order.getStatus())) {
                List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
                for (OrderItem item : items) {
                    List<ProductItem> linkedItems = item.getProductItems();
                    if (linkedItems == null || linkedItems.isEmpty()) {
                        continue;
                    }

                    // Restore exactly the ordered quantity that was deactivated when processing started.
                    List<Long> linkedIds = linkedItems.stream()
                            .limit(item.getQuantity())
                            .map(ProductItem::getId)
                            .toList();
                    int restored = productItemDAO.activateItemsByIds(linkedIds);
                    if (restored < linkedIds.size()) {
                        throw new IllegalStateException("Failed to restore stock for order item ID: " + item.getId());
                    }
                }
            }
        } else {
            throw new SecurityException("You don't have permission to cancel orders");
        }

        // Update status and note
        orderDAO.updateStatus(orderId, "CANCELLED", userId, note);

        if (note != null && !note.trim().isEmpty()) {
            orderDAO.updateNote(orderId, "CANCELLED: " + note);
        }
    }

    /**
     * Add or update order item - CHANGED to use productId
     * Returns error message instead of throwing exception
     */
    public String addOrUpdateOrderItem(Long orderId, Long productId, int quantity) {
        try {
            // Validate order
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                return "Order not found";
            }

            if (!"DRAFT".equals(order.getStatus())) {
                return "Can only add items to draft orders";
            }

            // Get product
            Product product = productDAO.findById(productId);
            if (product == null) {
                return "Product not found";
            }

            // Check total available quantity first
            int totalAvailable = productItemDAO.countActiveByProductId(productId);

            if (totalAvailable < quantity) {
                return "Insufficient stock. Available: " + totalAvailable + ", Requested: " + quantity;
            }

            // Check if this product already exists in order by checking all items
            List<OrderItem> existingItems = orderItemDAO.findByOrderId(orderId);
            OrderItem existing = existingItems.stream()
                    .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                // Update quantity
                int newQuantity = existing.getQuantity() + quantity;

                // Verify total quantity doesn't exceed available
                if (newQuantity > totalAvailable) {
                    return "Cannot add " + quantity + " more. Maximum available: " +
                            (totalAvailable - existing.getQuantity());
                }

                orderItemDAO.updateQuantity(existing.getId(), newQuantity);
            } else {
                // Add new item
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);

                orderItemDAO.addItem(orderItem);
            }

            orderDAO.refreshOrderFinalTotal(orderId);

            return null; // Success - no error

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add item: " + e.getMessage();
        }
    }

    public BigDecimal calculateOrderTotal(Long orderId) {
        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);

        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            if (item.getPriceAtPurchase() == null) {
                throw new RuntimeException("Price not set for item ID: " + item.getId());
            }

            BigDecimal itemTotal = item.getPriceAtPurchase()
                    .multiply(new BigDecimal(item.getQuantity()));

            total = total.add(itemTotal);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateSubtotal(Long orderId) {
        return calculateOrderTotal(orderId);
    }

    public BigDecimal calculateFinalTotal(Long orderId) {
        BigDecimal subtotal = calculateOrderTotal(orderId);
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    public void submitOrder(Long orderId, Long userId) {
        Order order = orderDAO.findById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (!order.getCreatedBy().getId().equals(userId)) {
            throw new SecurityException("You can only submit your own orders");
        }

        if (!"DRAFT".equals(order.getStatus())) {
            throw new IllegalStateException("Order is not in draft status");
        }

        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Cannot submit empty order");
        }

        orderDAO.updateStatus(orderId, "SUBMITTED", userId, null);
    }

    public List<Order> getOrdersBySalesman(Long salesmanId) {
        List<Order> orders = orderDAO.findBySalesman(salesmanId);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("No orders found for salesman");
        }
        return orders;
    }

    public boolean updateStatusOrder(OrderDTO orderDTO) {
        return orderDAO.updateStatus(orderDTO.getCode(), orderDTO.getStatus());
    }

    public int countOrdersByRole(Long userId, String roleName, String status, String searchKeyword) {
        return orderDAO.countOrdersByRole(userId, roleName, status, searchKeyword);
    }

    public List<Order> searchOrdersByRole(Long userId, String roleName, String status, String searchKeyword,
                                          String sortBy, String sortDir, int offset, int pageSize) {
        return orderDAO.searchOrdersByRole(userId, roleName, status, searchKeyword, sortBy, sortDir, offset, pageSize);
    }

    public int countOrders(String status, String searchCode) {
        return orderDAO.countOrders(status, searchCode);
    }

    public List<Order> getOrders(String status, String searchCode, String sortBy, String sortDir, int offset, int pageSize) {
        return orderDAO.getOrders(status, searchCode, sortBy, sortDir, offset, pageSize);
    }

    public int countOrdersBySalesman(Long salesmanId, String status, String searchCode) {
        return orderDAO.countOrdersBySalesman(salesmanId, status, searchCode);
    }

    public List<Order> getOrdersBySalesman(Long salesmanId, String status, String searchCode, String sortBy, String sortDir, int offset, int limit) {
        return orderDAO.getOrdersBySalesman(salesmanId, status, searchCode, sortBy, sortDir, offset, limit);
    }
    // Count orders by date range
    public int countOrdersByDateRange(Timestamp startDate, Timestamp endDate) {
        return orderDAO.countByDateRange(startDate, endDate);
    }

    // Count orders by salesman and date
    public int countOrdersBySalesmanAndDate(Long salesmanId, Timestamp startDate, Timestamp endDate) {
        return orderDAO.countBySalesmanAndDateRange(salesmanId, startDate, endDate);
    }

    // Count orders by salesman and status
    public int countOrdersBySalesmanAndStatus(Long salesmanId, String status) {
        return orderDAO.countBySalesmanAndStatus(salesmanId, status);
    }

    // Get orders by salesman and date
    public List<Order> getOrdersBySalesmanAndDate(Long salesmanId, Timestamp startDate, Timestamp endDate) {
        return orderDAO.findBySalesmanAndDateRange(salesmanId, startDate, endDate);
    }

    // Get recent orders by salesman
    public List<Order> getRecentOrdersBySalesman(Long salesmanId, Timestamp since, int limit) {
        return orderDAO.findRecentBySalesman(salesmanId, since, limit);
    }

    // Get recent orders (all)
    public List<Order> getRecentOrders(int limit) {
        return orderDAO.findRecentOrders(limit);
    }

    // Count orders by status and date
    public int countOrdersByStatusAndDate(String status, Timestamp startDate, Timestamp endDate) {
        return orderDAO.countByStatusAndDateRange(status, startDate, endDate);
    }

    // Get orders by status
    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.findByStatus(status);
    }

    public int countExportHistory(String searchCode, LocalDate fromDate, LocalDate toDate) {
        return orderDAO.countExportHistory(searchCode, fromDate, toDate);
    }

    public List<Order> getExportHistoryOrders(String searchCode, LocalDate fromDate, LocalDate toDate, int offset) {
        return orderDAO.getExportHistoryOrders(searchCode, fromDate, toDate, offset);
    }

    public ExportDTO getExportHistoryDetail(Long orderId) {
        ExportDTO exportDTO = orderDAO.getExportOrderHeader(orderId);
        if (exportDTO == null) return null;
        exportDTO.setItems(orderDAO.getExportOrderItems(orderId));
        return exportDTO;
    }
}