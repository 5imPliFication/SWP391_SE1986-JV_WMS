package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Coupon;
import com.example.model.Order;
import com.example.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {

    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getLong("id"));
        coupon.setCode(rs.getString("code"));
        coupon.setDescription(rs.getString("description"));
        coupon.setDiscountType(rs.getString("discount_type"));
        coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
        coupon.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
        coupon.setValidFrom(rs.getTimestamp("valid_from"));
        coupon.setValidUntil(rs.getTimestamp("valid_until"));
        coupon.setUsageLimit(rs.getObject("usage_limit") != null ? rs.getInt("usage_limit") : null);
        coupon.setUsedCount(rs.getInt("used_count"));
        coupon.setIsActive(rs.getBoolean("is_active"));
        coupon.setCreatedAt(rs.getTimestamp("created_at"));
        return coupon;
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
            coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
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
                         o.discount_amount,
                         o.final_total,
                 
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
                order.setTotal(rs.getBigDecimal("total"));
                order.setDiscountAmount(rs.getBigDecimal("discount_amount") != null ?
                        rs.getBigDecimal("discount_amount") : BigDecimal.ZERO);
                order.setFinalTotal(rs.getBigDecimal("final_total") != null ?
                        rs.getBigDecimal("final_total") : order.getTotal());

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
                    coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
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
        SELECT
            o.id AS order_id,
            o.order_code,
            o.customer_name,
            o.customer_phone,
            o.note,
            o.status,
            o.total_price as total,
            o.order_date,
            o.processed_at,

            u.id AS created_user_id,
            u.fullname AS created_user_name,

            c.id AS coupon_id,
            c.code AS coupon_code,
            c.discount_type,
            c.discount_value
        FROM orders o
        JOIN users u ON o.created_by = u.id
        LEFT JOIN coupons c ON o.coupon_id = c.id
        WHERE o.created_by = ?
        ORDER BY o.order_date DESC
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
        SELECT
            o.id AS order_id,
            o.order_code,
            o.customer_name,
            o.customer_phone,
            o.note,
            o.status,
            o.total_price as total,
            o.order_date,
            o.processed_at,

            u.id AS created_user_id,
            u.fullname AS created_user_name,

            c.id AS coupon_id,
            c.code AS coupon_code,
            c.discount_type,
            c.discount_value
        FROM orders o
        JOIN users u ON o.created_by = u.id
        LEFT JOIN coupons c ON o.coupon_id = c.id
        WHERE o.status = ?
        ORDER BY o.order_date ASC
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

    public boolean updateStatus(Long orderId,
                                String status,
                                Long processedBy,
                                String note) {

        String sql = """
        UPDATE orders
        SET status = ?,
            processed_by = ?,
            processed_at = ?,
            note = ?
        WHERE id = ?
    """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            if (processedBy != null) {
                ps.setLong(2, processedBy);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                ps.setNull(2, Types.BIGINT);
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setString(4, note);
            ps.setLong(5, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Update order status failed", e);
        }
    }

    public void updateNote(Long orderId, String note) {
        String sql = "UPDATE orders SET note = ?, processed_at = NOW() WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, note);
            ps.setLong(2, orderId);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Order not found with ID: " + orderId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order note", e);
        }
    }

    private BigDecimal calculateDiscountAmount(Coupon coupon, BigDecimal subtotal) {
        if ("PERCENTAGE".equals(coupon.getDiscountType())) {
            // Percentage discount: subtotal * (discountValue / 100)
            BigDecimal percentage = coupon.getDiscountValue().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            return subtotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
        } else {
            // Fixed amount discount
            BigDecimal fixedDiscount = coupon.getDiscountValue();

            // Don't let discount exceed subtotal
            if (fixedDiscount.compareTo(subtotal) > 0) {
                return subtotal;
            }

            return fixedDiscount;
        }
    }

    /**
     * Apply coupon to order
     */
    public void applyCoupon(Long orderId, Long couponId) {
        String selectOrderSql = "SELECT SUM(oi.quantity * p.price) as subtotal " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";

        String selectCouponSql = "SELECT * FROM coupons WHERE id = ?";

        String updateOrderSql = "UPDATE orders SET coupon_id = ?, discount_amount = ?, final_total = ? WHERE id = ?";

        String incrementUsageSql = "UPDATE coupons SET used_count = used_count + 1 WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection()) {

            // Start transaction
            con.setAutoCommit(false);

            try {
                // 1. Calculate order subtotal
                BigDecimal subtotal = BigDecimal.ZERO;
                try (PreparedStatement ps = con.prepareStatement(selectOrderSql)) {
                    ps.setLong(1, orderId);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        subtotal = rs.getBigDecimal("subtotal");
                        if (subtotal == null) {
                            subtotal = BigDecimal.ZERO;
                        }
                    }
                }

                if (subtotal.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalStateException("Cannot apply coupon to empty order");
                }

                // 2. Get coupon details
                Coupon coupon = null;
                try (PreparedStatement ps = con.prepareStatement(selectCouponSql)) {
                    ps.setLong(1, couponId);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        coupon = mapResultSetToCoupon(rs);
                    } else {
                        throw new IllegalArgumentException("Coupon not found");
                    }
                }

                // 3. Calculate discount amount
                BigDecimal discountAmount = calculateDiscountAmount(coupon, subtotal);

                // 4. Calculate final total
                BigDecimal finalTotal = subtotal.subtract(discountAmount);

                // Ensure final total is not negative
                if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
                    finalTotal = BigDecimal.ZERO;
                }

                // 5. Update order with coupon, discount, and final total
                try (PreparedStatement ps = con.prepareStatement(updateOrderSql)) {
                    ps.setLong(1, couponId);
                    ps.setBigDecimal(2, discountAmount);
                    ps.setBigDecimal(3, finalTotal);
                    ps.setLong(4, orderId);

                    int affected = ps.executeUpdate();

                    if (affected == 0) {
                        throw new SQLException("Order not found with ID: " + orderId);
                    }
                }

                // 6. Increment coupon usage count
                try (PreparedStatement ps = con.prepareStatement(incrementUsageSql)) {
                    ps.setLong(1, couponId);
                    ps.executeUpdate();
                }

                // Commit transaction
                con.commit();

            } catch (Exception e) {
                // Rollback on error
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to apply coupon to order", e);
        }
    }

    /**
     * Remove coupon from order
     */
    public void removeCoupon(Long orderId) {
        String selectOrderSql = "SELECT SUM(oi.quantity * p.price) as subtotal, o.coupon_id " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "JOIN orders o ON o.id = oi.order_id " +
                "WHERE oi.order_id = ? " +
                "GROUP BY o.coupon_id";

        String updateOrderSql = "UPDATE orders SET coupon_id = NULL, discount_amount = 0, final_total = ? WHERE id = ?";

        String decrementUsageSql = "UPDATE coupons SET used_count = used_count - 1 WHERE id = ? AND used_count > 0";

        try (Connection con = DBConfig.getDataSource().getConnection()) {

            // Start transaction
            con.setAutoCommit(false);

            try {
                // 1. Get current order subtotal and coupon ID
                BigDecimal subtotal = BigDecimal.ZERO;
                Long couponId = null;

                try (PreparedStatement ps = con.prepareStatement(selectOrderSql)) {
                    ps.setLong(1, orderId);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        subtotal = rs.getBigDecimal("subtotal");
                        if (subtotal == null) {
                            subtotal = BigDecimal.ZERO;
                        }
                        couponId = rs.getObject("coupon_id") != null ? rs.getLong("coupon_id") : null;
                    }
                }

                // 2. Update order - remove coupon and set final_total to subtotal
                try (PreparedStatement ps = con.prepareStatement(updateOrderSql)) {
                    ps.setBigDecimal(1, subtotal); // final_total = subtotal (no discount)
                    ps.setLong(2, orderId);

                    int affected = ps.executeUpdate();

                    if (affected == 0) {
                        throw new SQLException("Order not found with ID: " + orderId);
                    }
                }

                // 3. Decrement coupon usage count if there was a coupon
                if (couponId != null) {
                    try (PreparedStatement ps = con.prepareStatement(decrementUsageSql)) {
                        ps.setLong(1, couponId);
                        ps.executeUpdate();
                    }
                }

                // Commit transaction
                con.commit();

            } catch (Exception e) {
                // Rollback on error
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove coupon from order", e);
        }
    }
}
