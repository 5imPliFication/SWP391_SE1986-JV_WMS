package com.example.service;

import com.example.dao.OrderDAO;
import com.example.dao.OrderItemDAO;
import com.example.dao.ProductDAO;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        productDAO = new ProductDAO();
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
        order.setCreatedBy(salesmanId);
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


    public void submitOrder(Long orderId, Long salesmanId) {
        Order order = orderDAO.findById(orderId);

        List<OrderItem> orderItems = orderItemDAO.findByOrderId(orderId);
        if (orderItems.isEmpty()) {
            throw new IllegalStateException("Cannot submit an order with no items");
        }

        if (!order.getCreatedBy().equals(salesmanId))
            throw new SecurityException("Not your order");

        if (!order.getStatus().equals("DRAFT"))
            throw new IllegalStateException("Order already submitted");

        orderDAO.updateStatus(orderId, "SUBMITTED", salesmanId, null);
    }

    public List<Order> getOrdersBySalesman(Long salesmanId) {
        return orderDAO.findBySalesman(salesmanId);
    }

    /* ================= SHARED ================= */

    public Order getOrderDetail(Long orderId, Long userId, String role) {
        Order order = orderDAO.findById(orderId);
        if (order == null) return null;

        if ("Salesman".equals(role) && !Objects.equals(order.getCreatedBy(), userId))
            throw new SecurityException("Access denied");

        return order;
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

    public void cancelOrder(Long orderId, Long warehouseKeeperId, String note) {
        if (note == null || note.isBlank())
            throw new IllegalArgumentException("Flag reason required");

        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("PROCESSING"))
            throw new IllegalStateException("Order not in processing");

        orderDAO.updateStatus(orderId, "CANCELLED", warehouseKeeperId, note);
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
}
