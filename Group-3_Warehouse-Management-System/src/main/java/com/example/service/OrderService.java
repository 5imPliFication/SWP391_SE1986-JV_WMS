package com.example.service;

import com.example.dao.OrderDAO;
import com.example.dao.OrderItemDAO;
import com.example.model.Order;
import com.example.model.OrderItem;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
    }

    /* ================= SALESMAN ================= */

    public int createDraftOrder(String customerName, String customerPhone,  String note, Long salesmanId) {
        Order order = new Order();
        order.setOrderCode("ORD-" + UUID.randomUUID());
        order.setCustomerName(customerName);
        order.setCustomerPhone(customerPhone);
        order.setStatus("DRAFT");
        order.setNote(note);
        order.setCreatedBy(salesmanId);
        System.out.println("Service layer ----------");
        System.out.println(order.getOrderCode()+" ++ "+order.getStatus()+" ++ "+order.getCreatedBy());
        return orderDAO.create(order);
    }

    public void addItem(Long orderId, Long productId, Integer quantity, Long salesmanId) {
        Order order = orderDAO.findById(orderId);

        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if (!order.getStatus().equals("DRAFT"))
            throw new IllegalStateException("Order is not editable");

        if (!Objects.equals(order.getCreatedBy(), salesmanId))
            throw new SecurityException("Access denied");

        OrderItem item = new OrderItem();
        item.setOrderId(orderId);
        item.setProductId(productId);
        item.setQuantity(quantity);

        orderItemDAO.addItem(item);
    }

    public void submitOrder(Long orderId, Long salesmanId) {
        Order order = orderDAO.findById(orderId);

        if (!order.getCreatedBy().equals(salesmanId))
            throw new SecurityException("Not your order");

        if (!order.getStatus().equals("DRAFT"))
            throw new IllegalStateException("Order already submitted");

        orderDAO.updateStatus(orderId, "SUBMITTED", null, null);
    }

    public List<Order> getOrdersBySalesman(Long salesmanId) {
        return orderDAO.findBySalesman(salesmanId);
    }

    /* ================= SHARED ================= */

    public Order getOrderDetail(Long orderId, Long userId, String role) {
        Order order = orderDAO.findById(orderId);
        if (order == null) return null;

        if ("SALESMAN".equals(role) && !Objects.equals(order.getCreatedBy(), userId))
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

    public void approveOrder(Long orderId, Long warehouseKeeperId) {
        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("PROCESSING"))
            throw new IllegalStateException("Order not in processing");

        orderDAO.updateStatus(orderId, "APPROVED", warehouseKeeperId, null);
    }

    public void flagOrder(Long orderId, Long warehouseKeeperId, String note) {
        if (note == null || note.isBlank())
            throw new IllegalArgumentException("Flag reason required");

        Order order = orderDAO.findById(orderId);

        if (!order.getStatus().equals("PROCESSING"))
            throw new IllegalStateException("Order not in processing");

        orderDAO.updateStatus(orderId, "FLAGGED", warehouseKeeperId, note);
    }
}
