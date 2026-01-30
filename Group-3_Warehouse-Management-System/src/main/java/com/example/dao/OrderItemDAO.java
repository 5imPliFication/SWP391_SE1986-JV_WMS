package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public void addItem(OrderItem item) {
        String sql = """
            INSERT INTO order_items (order_id, product_id, quantity)
            VALUES (?, ?, ?)
        """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, item.getOrderId());
            ps.setLong(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Add order item failed", e);
        }
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = """
            SELECT oi.*, p.name AS product_name
            FROM order_items oi
            JOIN product p ON oi.product_id = p.id
            WHERE oi.order_id = ?
        """;

        List<OrderItem> list = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getLong("id"));
                item.setOrderId(rs.getLong("order_id"));
                item.setProductId(rs.getLong("product_id"));
                item.setQuantity(rs.getInt("quantity"));
//                item.setProductName(rs.getString("product_name")); // optional
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find order items failed", e);
        }
        return list;
    }

    public boolean deleteByOrderId(Long orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Delete order items failed", e);
        }
    }
}
