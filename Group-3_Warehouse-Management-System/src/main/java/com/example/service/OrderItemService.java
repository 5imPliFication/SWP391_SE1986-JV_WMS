package com.example.service;

import com.example.dao.OrderDAO;
import com.example.dao.OrderItemDAO;
import com.example.model.Order;
import com.example.model.OrderItem;

import java.util.List;
import java.util.Objects;

public class OrderItemService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;

    public OrderItemService() {
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
    }

    public List<OrderItem> getItemsByOrder(Long orderId, Long userId, String role) {
        Order order = orderDAO.findById(orderId);

        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if ("Salesman".equalsIgnoreCase(role)
                && (order.getCreatedBy() == null || !Objects.equals(order.getCreatedBy().getId(), userId)))
            throw new SecurityException("Access denied");

        return orderItemDAO.findByOrderId(orderId);
    }

    public int countItemsByOrder(Long orderId, Long userId, String role) {
        Order order = orderDAO.findById(orderId);

        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if ("Salesman".equalsIgnoreCase(role)
                && (order.getCreatedBy() == null || !Objects.equals(order.getCreatedBy().getId(), userId)))
            throw new SecurityException("Access denied");

        return orderItemDAO.countByOrderId(orderId);
    }

    public List<OrderItem> getItemsByOrder(Long orderId, Long userId, String role, int offset, int limit) {
        Order order = orderDAO.findById(orderId);

        if (order == null)
            throw new IllegalArgumentException("Order not found");

        if ("Salesman".equalsIgnoreCase(role)
                && (order.getCreatedBy() == null || !Objects.equals(order.getCreatedBy().getId(), userId)))
            throw new SecurityException("Access denied");

        return orderItemDAO.findByOrderId(orderId, offset, limit);
    }

    public void updateQuantity(Long orderId, Long itemId, int quantity) {
        orderItemDAO.updateQuantity(itemId, quantity);
        orderDAO.refreshOrderFinalTotal(orderId);
    }
}
