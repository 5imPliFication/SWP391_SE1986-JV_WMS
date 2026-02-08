package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Coupon;
import com.example.model.Order;
import com.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {

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

        Order order = new Order();
        order.setId(rs.getLong("order_id"));
        order.setOrderCode(rs.getString("order_code"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerPhone(rs.getString("customer_phone"));
        order.setNote(rs.getString("note"));

        // ---- status (enum-safe) ----
        order.setStatus(rs.getString("status"));

        // ---- createdBy user ----
        User createdBy = new User();
        createdBy.setId(rs.getLong("created_user_id"));
        createdBy.setFullName(rs.getString("created_user_name"));
        order.setCreatedBy(createdBy);

        // ---- createdAt ----
        Timestamp createdAt = rs.getTimestamp("order_date");
        if (createdAt != null) {
            order.setCreatedAt(createdAt);
        }

        // ---- processedAt (nullable) ----
        Timestamp processedAt = rs.getTimestamp("processed_at");
        if (processedAt != null) {
            order.setProcessedAt(processedAt);
        }

        // ---- coupon (nullable) ----
        long couponId = rs.getLong("coupon_id");
        if (!rs.wasNull()) {
            Coupon coupon = new Coupon();
            coupon.setId(couponId);
            coupon.setCode(rs.getString("coupon_code"));
            coupon.setDiscountType(rs.getString("discount_type"));
            coupon.setDiscountValue(rs.getDouble("discount_value"));
            order.setCoupon(coupon);
        }

        return order;
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
            ps.setLong(6, order.getCreatedBy().getId());

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
         String sql = """
                     SELECT
                         o.id            AS order_id,
                         o.order_code,
                         o.customer_name,
                         o.customer_phone,
                         o.note,
                         o.status,
                         o.total_price as total,
                         o.order_date,
                         o.processed_at,
                 
                         cu.id           AS created_user_id,
                         cu.fullname     AS created_user_name,
                 
                         pu.id           AS processed_user_id,
                         pu.fullname     AS processed_user_name,
                 
                         c.id            AS coupon_id,
                         c.code          AS coupon_code,
                         c.discount_type,
                         c.discount_value
                     FROM orders o
                     JOIN users cu ON o.created_by = cu.id
                     LEFT JOIN users pu ON o.processed_by = pu.id
                     LEFT JOIN coupons c ON o.coupon_id = c.id
                     WHERE o.id = ?
                 """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                Order order = new Order();
                order.setId(rs.getLong("order_id"));
                order.setOrderCode(rs.getString("order_code"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerPhone(rs.getString("customer_phone"));
                order.setNote(rs.getString("note"));
                order.setStatus(rs.getString("status"));
                order.setTotal(rs.getDouble("total"));

                // ----- createdBy -----
                User createdBy = new User();
                createdBy.setId(rs.getLong("created_user_id"));
                createdBy.setFullName(rs.getString("created_user_name"));
                order.setCreatedBy(createdBy);

                Timestamp createdAt = rs.getTimestamp("order_date");
                if (createdAt != null) {
                    order.setCreatedAt(createdAt);
                }

                // ----- processedBy (nullable) -----
                Long processedUserId = rs.getLong("processed_user_id");
                if (!rs.wasNull()) {
                    User processedBy = new User();
                    processedBy.setId(processedUserId);
                    processedBy.setFullName(rs.getString("processed_user_name"));
                    order.setProcessedBy(processedBy);
                }

                Timestamp processedAt = rs.getTimestamp("processed_at");
                if (processedAt != null) {
                    order.setProcessedAt(processedAt);
                }

                // ----- coupon (nullable) -----
                Long couponId = rs.getLong("coupon_id");
                if (!rs.wasNull()) {
                    Coupon coupon = new Coupon();
                    coupon.setId(couponId);
                    coupon.setCode(rs.getString("coupon_code"));
                    coupon.setDiscountType(rs.getString("discount_type"));
                    coupon.setDiscountValue(rs.getDouble("discount_value"));
                    order.setCoupon(coupon);
                }

                return order;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order with id: " + orderId, e);
        }
    }


    public List<Order> findAll() {
        String sql = "SELECT\n" +
                "    o.id            AS order_id,\n" +
                "    o.order_code,\n" +
                "    o.customer_name,\n" +
                "    o.customer_phone,\n" +
                "    o.note,\n" +
                "    o.status,\n" +
                "    o.total_price as total,\n" +
                "    o.order_date,\n" +
                "    o.processed_at,\n" +
                "\n" +
                "    u.id            AS created_user_id,\n" +
                "    u.fullname      AS created_user_name,\n" +
                "\n" +
                "    c.id            AS coupon_id,\n" +
                "    c.code          AS coupon_code,\n" +
                "    c.discount_type,\n" +
                "    c.discount_value\n" +
                "FROM orders o\n" +
                "JOIN users u ON o.created_by = u.id\n" +
                "LEFT JOIN coupons c ON o.coupon_id = c.id\n" +
                "WHERE o.status not like 'DRAFT' " +
                "ORDER BY o.order_date DESC\n";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(mapOrder(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all orders", e);
        }

        return orders;
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
