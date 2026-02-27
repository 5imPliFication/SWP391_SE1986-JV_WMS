package com.example.service;

import com.example.dao.*;
import com.example.model.*;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductItemDAO productItemDAO;  // ✓ ADDED
    private final CouponDAO couponDAO;
    private final UserDAO userDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        productItemDAO = new ProductItemDAO();  // ✓ ADDED
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

    public void startProcessing(Long orderId, Long warehouseKeeperId) {
        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("SUBMITTED"))
            throw new IllegalStateException("Order is not in queue");

        orderDAO.updateStatus(orderId, "PROCESSING", warehouseKeeperId, null);
    }

    public void completeProcessing(Long orderId, Long warehouseKeeperId) {
        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("PROCESSING"))
            throw new IllegalStateException("Order is not in queue");

        orderDAO.updateStatus(orderId, "COMPLETED", warehouseKeeperId, null);
    }

    // Salesman cancel (no note required)
    public void cancelOrder(Long orderId, Long userId) {
        cancelOrder(orderId, userId, null);
    }

    // Warehouse cancel (with note)
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
     * Add or update order item
     * ✓ FIXED: Now uses ProductItemDAO and stores price_at_purchase
     */
    public void addOrUpdateOrderItem(Long orderId, Long productItemId, int quantity) {
        // Validate order
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (!"DRAFT".equals(order.getStatus())) {
            throw new IllegalStateException("Can only add items to draft orders");
        }

        ProductItem productItem = productItemDAO.findById(productItemId);
        if (productItem == null) {
            throw new IllegalArgumentException("Product item not found");
        }

        // Check if item already exists
        OrderItem existing = orderItemDAO.findByOrderAndProductItem(orderId, productItemId);

        if (existing != null) {
            // Update quantity
            int newQuantity = existing.getQuantity() + quantity;

            orderItemDAO.updateQuantity(existing.getId(), newQuantity);
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductItem(productItem);
            orderItem.setQuantity(quantity);

            orderItemDAO.addItem(orderItem);
        }
    }

    /**
     * Calculate total amount for an order
     * ✓ FIXED: Now uses price_at_purchase from order_items
     */
    public BigDecimal calculateOrderTotal(Long orderId) {
        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);

        if (items == null || items.isEmpty()) {
            System.out.println("Order Empty!!!");
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            // ✓ FIXED: Use price_at_purchase instead of product.price
            if (item.getPriceAtPurchase() == null) {
                throw new RuntimeException("Price not set for item ID: " + item.getId());
            }

            BigDecimal itemTotal = item.getPriceAtPurchase()
                    .multiply(new BigDecimal(item.getQuantity()));

            total = total.add(itemTotal);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate subtotal (before discount)
     */
    public BigDecimal calculateSubtotal(Long orderId) {
        return calculateOrderTotal(orderId);
    }

    /**
     * Calculate discount amount
     */
    public BigDecimal calculateDiscountAmount(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null || order.getCoupon() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal subtotal = calculateOrderTotal(orderId);
        return calculateDiscount(order.getCoupon(), subtotal);
    }

    /**
     * Calculate final total (after coupon discount)
     */
    public BigDecimal calculateFinalTotal(Long orderId) {
        BigDecimal subtotal = calculateOrderTotal(orderId);
        BigDecimal discount = calculateDiscountAmount(orderId);

        return subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate discount amount based on coupon
     */
    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderTotal) {
        if ("PERCENTAGE".equals(coupon.getDiscountType())) {
            // Percentage discount
            BigDecimal percentage = coupon.getDiscountValue()
                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            return orderTotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
        } else {
            // Fixed amount discount (but not more than order total)
            BigDecimal discount = coupon.getDiscountValue();
            return discount.min(orderTotal);
        }
    }

    /**
     * Apply coupon to order
     */
    public void applyCouponToOrder(Long orderId, Long couponId) {
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

        BigDecimal orderTotal = calculateOrderTotal(orderId);

        if (coupon.getMinOrderAmount() != null &&
                orderTotal.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new IllegalArgumentException(
                    String.format("Order total must be at least %s VND to use this coupon",
                            coupon.getMinOrderAmount().toPlainString())
            );
        }

        orderDAO.applyCoupon(orderId, couponId);
    }

    /**
     * Remove coupon from order
     */
    public void removeCouponFromOrder(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (!"DRAFT".equals(order.getStatus())) {
            throw new IllegalStateException("Can only remove coupons from draft orders");
        }

        orderDAO.removeCoupon(orderId);
    }

    /**
     * Submit order
     * ✓ FIXED: Proper equality check and null safety
     */
    public void submitOrder(Long orderId, Long userId) {
        Order order = orderDAO.findById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        // ✓ FIXED: Compare Long with Long, not User with Long
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

        // ✓ FIXED: Check if coupon exists before accessing
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

    public List<Order> getOrders(String status, String searchCode, int offset, int pageSize) {
        return orderDAO.getOrders(status, searchCode, offset, pageSize);
    }

    public int countOrdersBySalesman(Long salesmanId, String status, String searchCode) {
        return orderDAO.countOrdersBySalesman(salesmanId, status, searchCode);
    }

    public List<Order> getOrdersBySalesman(Long salesmanId, String status, String searchCode, int offset, int limit) {
        return orderDAO.getOrdersBySalesman(salesmanId, status, searchCode, offset, limit);
    }
}
