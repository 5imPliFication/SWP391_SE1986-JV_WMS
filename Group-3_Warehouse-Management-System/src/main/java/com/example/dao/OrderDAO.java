package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setOrderCode(rs.getString("order_code"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setStatus(rs.getString("status"));
        o.setCreatedBy(rs.getLong("created_by"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setProcessedBy(rs.getLong("processed_by"));
        o.setProcessedAt(rs.getTimestamp("processed_at"));
        o.setNote(rs.getString("note"));
        return o;
    }

    public int create(Order order) {
        String sql = """
            INSERT INTO orders (order_code, customer_name, status, created_by)
            VALUES (?, ?, ?, ?)
            RETURNING id
        """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, order.getOrderCode());
            ps.setString(2, order.getCustomerName());
            ps.setString(3, order.getStatus());
            ps.setLong(4, order.getCreatedBy());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Create order failed", e);
        }
        return -1;
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

    public List<Order> findBySalesman(int salesmanId) {
        String sql = """
            SELECT * FROM orders
            WHERE created_by = ?
            ORDER BY created_at DESC
        """;

        List<Order> list = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, salesmanId);
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
            ORDER BY created_at ASC
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
