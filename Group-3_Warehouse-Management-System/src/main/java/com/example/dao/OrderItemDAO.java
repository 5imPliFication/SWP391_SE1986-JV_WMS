package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.Product;

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

            ps.setLong(1, item.getOrder().getId());
            ps.setLong(2, item.getProduct().getId());
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Add order item failed", e);
        }
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = """
        SELECT oi.id, oi.quantity,
               p.id AS product_id, p.name AS product_name, 
               p.description, p.price AS product_price
        FROM order_items oi
        JOIN products p ON oi.product_id = p.id
        WHERE oi.order_id = ?
        ORDER BY oi.id
    """;

        List<OrderItem> items = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Create Product
                    Product product = new Product();
                    product.setId(rs.getLong("product_id"));
                    product.setName(rs.getString("product_name"));
                     product.setDescription(rs.getString("description"));
//                     product.setPrice(rs.getBigDecimal("product_price"));

                    // Create OrderItem
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order items for order ID: " + orderId, e);
        }

        return items;
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

    public OrderItem findByOrderAndProduct(Long orderId, Long productId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ? AND product_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ps.setLong(2, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getLong("id"));
                item.setQuantity(rs.getInt("quantity"));
                return item;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order item", e);
        }

        return null;
    }

    public void updateQuantity(Long itemId, int quantity) {
        String sql = "UPDATE order_items SET quantity = ? WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setLong(2, itemId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update quantity", e);
        }
    }
}
