package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.dto.ExportOrderDTO;
import java.util.Map;

public class OrderDAO {

    public List<Order> findAll() {
        String sql = """
        SELECT o.*, u.fullname AS created_by_name
        FROM orders o
        LEFT JOIN users u ON o.created_by = u.id
        WHERE o.status not like 'DRAFT'
        ORDER BY o.order_date DESC
    """;

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setOrderCode(rs.getString("order_code"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerPhone(rs.getString("customer_phone"));
                order.setNote(rs.getString("note"));
                order.setStatus(rs.getString("status"));
                order.setCreatedBy(rs.getLong("created_by"));
                order.setCreatedAt(rs.getTimestamp("order_date"));
                order.setProcessedAt(rs.getTimestamp("processed_at"));

                orders.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all orders", e);
        }

        return orders;
    }

    public Map<String, Integer> getStatistics() {
        String sql = """
        SELECT status, COUNT(*) as count
        FROM orders
        GROUP BY status
    """;

        Map<String, Integer> stats = new HashMap<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch statistics", e);
        }

        return stats;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setOrderCode(rs.getString("order_code"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setCustomerPhone(rs.getString("customer_phone"));
        o.setStatus(rs.getString("status"));
        o.setCreatedBy(rs.getLong("created_by"));
        o.setCreatedAt(rs.getTimestamp("order_date"));
        o.setProcessedBy(rs.getLong("processed_by"));
        o.setProcessedAt(rs.getTimestamp("processed_at"));
        o.setNote(rs.getString("note"));
        return o;
    }

    public int create(Order order) {
        String sql = """
        INSERT INTO orders (order_code, customer_name, customer_phone, note, status, created_by)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, order.getOrderCode());
            ps.setString(2, order.getCustomerName());
            ps.setString(3, order.getCustomerPhone());
            ps.setString(4, order.getNote());
            ps.setString(5, order.getStatus());
            ps.setLong(6, order.getCreatedBy());

            System.out.println("DAO LEVEL - Creating order");
            System.out.println("Order Code: " + order.getOrderCode());

            // Use executeUpdate() for INSERT statements
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Order created with ID: " + generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Create order failed", e);
        }
    }

    public Order findById(Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapOrder(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find order by id failed", e);
        }
        return null;
    }

    public List<Order> findBySalesman(Long salesmanId) {
        String sql = """
            SELECT * FROM orders
            WHERE created_by = ?
            ORDER BY order_date DESC
        """;

        List<Order> list = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, salesmanId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find orders by salesman failed", e);
        }
        return list;
    }

    public List<Order> findByStatus(String status) {
        String sql = """
            SELECT * FROM orders
            WHERE status = ?
            ORDER BY order_date ASC
        """;

        List<Order> list = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find orders by status failed", e);
        }
        return list;
    }

    public boolean updateStatus(Long orderId, String status, Long processedBy, String note) {
        String sql = """
            UPDATE orders
            SET status = ?, processed_by = ?, processed_at = NOW(), note = ?
            WHERE id = ?
        """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            if (processedBy != null) {
                ps.setLong(2, processedBy);
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setString(3, note);
            ps.setLong(4, orderId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update order status failed", e);
        }
    }
}
