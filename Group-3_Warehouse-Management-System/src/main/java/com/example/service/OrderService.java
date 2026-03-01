package com.example.service;

import com.example.dao.*;
import com.example.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductItemDAO productItemDAO;
    private final ProductDAO productDAO;
    private final CouponDAO couponDAO;
    private final UserDAO userDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        productItemDAO = new ProductItemDAO();
        productDAO = new ProductDAO();
        couponDAO = new CouponDAO();
        userDAO = new UserDAO();
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

    public void removeItem(Long orderId, Long productItemId) {
        Order order = orderDAO.findById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if (!"DRAFT".equals(order.getStatus()))
            throw new IllegalStateException("Order is not editable");

        OrderItem orderItem = orderItemDAO.findByOrderAndProductItem(orderId, productItemId);
        if (orderItem == null)
            throw new IllegalArgumentException("Item not found");

        orderItemDAO.deleteByOrderAndProductItem(orderId, productItemId);
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

        // Load coupon if applied
        if (order.getCoupon() != null) {
            couponDAO.findById(order.getCoupon().getId());
        }

        return order;
    }

    // Load creator info for a single order (when needed)
    public void loadCreator(Order order) {
        if (order.getCreatedBy() != null) {
            userDAO.findUserById(order.getCreatedBy().getId());
        }
    }

    // Load coupon info for a single order (when needed)
    public void loadCoupon(Order order) {
        if (order.getCoupon() != null) {
            couponDAO.findById(order.getCoupon().getId());
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

        // Reduce stock for each item
        for (OrderItem item : items) {
            ProductItem productItem = productItemDAO.findById(item.getProductItem().getId());
            if (productItem == null) {
                throw new IllegalStateException("Product item not found for order item ID: " + item.getId());
            }

            Long productId = productItem.getProductId();
            int available = productItemDAO.countActiveByProductId(productId);

            // Check if enough stock available
            if (available < item.getQuantity()) {
                Product product = productDAO.findById(productId);
                String productName = product != null ? product.getName() : ("Product ID " + productId);
                throw new IllegalStateException(
                        "Insufficient stock for " + productName +
                                ". Available: " + available +
                                ", Required: " + item.getQuantity()
                );
            }

            int deactivated = productItemDAO.deactivateAvailableItems(productId, item.getQuantity());
            if (deactivated < item.getQuantity()) {
                throw new IllegalStateException("Failed to reserve enough stock for product ID: " + productId);
            }
        }

        // Update order status
        orderDAO.updateStatus(orderId, "PROCESSING", warehouseKeeperId, null);
    }

    public void completeProcessing(Long orderId, Long warehouseKeeperId) {
        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("PROCESSING"))
            throw new IllegalStateException("Order is not in queue");

        orderDAO.updateStatus(orderId, "COMPLETED", warehouseKeeperId, null);
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

            // If order is PROCESSING, restore inventory
            if ("PROCESSING".equals(order.getStatus())) {
                List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
                for (OrderItem item : items) {
                    ProductItem productItem = productItemDAO.findById(item.getProductItem().getId());
                    if (productItem == null) {
                        continue;
                    }
                    int restored = productItemDAO.activateInactiveItems(productItem.getProductId(), item.getQuantity());
                    if (restored < item.getQuantity()) {
                        throw new IllegalStateException("Failed to restore stock for product ID: " + productItem.getProductId());
                    }
                }
            }
        } else {
            throw new SecurityException("You don't have permission to cancel orders");
        }

        // Update status and note
        orderDAO.updateStatus(orderId, "CANCELLED", userId, note);
        couponDAO.decrementUsageCount(orderId);

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

            // Get available product items for this product
            List<ProductItem> productItems = productItemDAO.findByProductId(productId);
            if (productItems == null || productItems.isEmpty()) {
                return "No available items for this product";
            }

            // Check total available quantity
            int totalAvailable = productItemDAO.countActiveByProductId(productId);

            if (totalAvailable < quantity) {
                return "Insufficient stock. Available: " + totalAvailable + ", Requested: " + quantity;
            }

            ProductItem selectedItem = productItems.stream()
                    .filter(item -> Boolean.TRUE.equals(item.getIsActive()))
                    .findFirst()
                    .orElse(null);

            if (selectedItem == null) {
                return "No available items in stock";
            }

            // Check if this product item already exists in order
            OrderItem existing = orderItemDAO.findByOrderAndProductItem(orderId, selectedItem.getId());

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
                orderItem.setProductItem(selectedItem);
                orderItem.setProduct(product); // For convenience
                orderItem.setQuantity(quantity);

                orderItemDAO.addItem(orderItem);
            }

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

    public BigDecimal calculateDiscountAmount(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null || order.getCoupon() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal subtotal = calculateOrderTotal(orderId);
        return calculateDiscount(order.getCoupon(), subtotal);
    }

    public BigDecimal calculateFinalTotal(Long orderId) {
        BigDecimal subtotal = calculateOrderTotal(orderId);
        BigDecimal discount = calculateDiscountAmount(orderId);

        return subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderTotal) {
        if ("PERCENTAGE".equals(coupon.getDiscountType())) {
            BigDecimal percentage = coupon.getDiscountValue()
                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            return orderTotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
        } else {
            BigDecimal discount = coupon.getDiscountValue();
            return discount.min(orderTotal);
        }
    }

    public void applyCouponToOrder(Long orderId, Long couponId, Long userId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (!"DRAFT".equals(order.getStatus())) {
            throw new IllegalStateException("Can only apply coupons to draft orders");
        }

        Coupon coupon = couponDAO.findById(couponId);
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon not found");
        }

        if (!coupon.isValid()) {
            throw new IllegalStateException("Coupon is no longer valid");
        }

        // Check if user has already used this coupon (per-user restriction)
        if (couponDAO.hasUserUsedCoupon(userId, couponId)) {
            throw new IllegalStateException("You have already used this coupon");
        }

        BigDecimal orderTotal = calculateOrderTotal(orderId);

        if (coupon.getMinOrderAmount() != null &&
                orderTotal.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new IllegalArgumentException(
                    String.format("Order total must be at least %s VND to use this coupon",
                            coupon.getMinOrderAmount().toPlainString())
            );
        }

        // Check if order already has a coupon and remove its usage tracking
        if (order.getCoupon() != null) {
            couponDAO.removeUserCouponUsage(userId, order.getCoupon().getId());
            couponDAO.decrementUsageCount(order.getCoupon().getId());
        }

        // Apply new coupon
        orderDAO.applyCoupon(orderId, couponId);
        
        // Record this user using this coupon
        couponDAO.recordUserCouponUsage(userId, couponId);
    }

    public void removeCouponFromOrder(Long orderId, Long userId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (!"DRAFT".equals(order.getStatus())) {
            throw new IllegalStateException("Can only remove coupons from draft orders");
        }

        // Only remove if order has a coupon
        if (order.getCoupon() != null) {
            // Remove user usage tracking
            couponDAO.removeUserCouponUsage(userId, order.getCoupon().getId());
        }

        orderDAO.removeCoupon(orderId);
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

        if (order.getCoupon() != null && order.getCoupon().getId() != null) {
            couponDAO.incrementUsageCount(order.getCoupon().getId());
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

    public boolean updateStatusOrder(String orderCode, String status) {
        return orderDAO.updateStatus(orderCode, status);
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
}