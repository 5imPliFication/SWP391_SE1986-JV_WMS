package com.example.service;

import com.example.dao.*;
import com.example.model.*;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;
    private final CouponDAO couponDAO;
    private final UserDAO userDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        productDAO = new ProductDAO();
        couponDAO = new CouponDAO();
        userDAO = new UserDAO();
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
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

    public void removeItem(Long orderId, Long productId) {

        Order order = orderDAO.findById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if (!"DRAFT".equals(order.getStatus()))
            throw new IllegalStateException("Order is not editable");

        OrderItem orderItem = orderItemDAO.findByOrderAndProduct(orderId, productId);
        if (orderItem == null)
            throw new IllegalArgumentException("Item not found");

        orderItemDAO.deleteByOrderAndProduct(orderId, productId);
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

        // Authorization check
        // Salesman can only cancel their own DRAFT orders
        // Warehouse can cancel SUBMITTED or PROCESSING orders

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

    public void addOrUpdateOrderItem(Long orderId, Long productId, int quantity) {
        // Check if item already exists
        OrderItem existing = orderItemDAO.findByOrderAndProduct(orderId, productId);

        if (existing != null) {
            // Update quantity
            existing.setQuantity(existing.getQuantity() + quantity);
            orderItemDAO.updateQuantity(existing.getId(), existing.getQuantity());
        } else {
            // Add new item
            OrderItem orderItem = new OrderItem();
            Order order = orderDAO.findById(orderId);
            Product product = productDAO.findById(productId);
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItemDAO.addItem(orderItem);
        }
    }

    /**
     * Calculate total amount for an order
     */
    public BigDecimal calculateOrderTotal(Long orderId) {
        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);

        if (items == null || items.isEmpty()) {
            System.out.println("Order Empty!!!");
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            if (item.getProduct() == null) {
                throw new RuntimeException("Product not loaded for item ID: " + item.getId());
            }

            BigDecimal itemTotal = item.getProduct().getPrice()
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
        if (coupon.getDiscountType().equals("PERCENTAGE")) {
            // Percentage discount
            BigDecimal percentage = coupon.getDiscountValue().divide(new BigDecimal("100"));
            return orderTotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
        } else {
            // Fixed amount discount
            return coupon.getDiscountValue();
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
        if (coupon==null) {
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

    public void submitOrder(Long orderId, Long userId) {
        Order order = orderDAO.findById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (!order.getCreatedBy().equals(userId)) {
            throw new SecurityException("You can only submit your own orders");
        }

        if (!"DRAFT".equals(order.getStatus())) {
            throw new IllegalStateException("Order is not in draft status");
        }

        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Cannot submit empty order");
        }

        // Increment coupon usage if coupon was applied
        if (order.getCoupon().getId() != null) {
            couponDAO.incrementUsageCount(order.getCoupon().getId());
        }

        orderDAO.updateStatus(orderId, "SUBMITTED", userId, null);
    }

    public List<Order> getOrdersBySalesman(Long salesmanId){
        List<Order> orders = orderDAO.findBySalesman(salesmanId);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        return orders;
    }
}
