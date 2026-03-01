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

        if ("SALESMAN".equals(role) && !Objects.equals(order.getCreatedBy(), userId))
            throw new SecurityException("Access denied");

        return orderItemDAO.findByOrderId(orderId);
    }
    public void updateQuantity(Long itemId, int quantity) {
        orderItemDAO.updateQuantity(itemId, quantity);
    }
}
