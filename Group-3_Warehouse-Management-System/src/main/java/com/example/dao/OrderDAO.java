package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Order;
import com.example.model.User;
import com.example.util.AppConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.dto.ExportDTO;
import com.example.dto.ExportProductDTO;
import com.example.dto.ExportProductItemDTO;

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
        order.setId(rs.getLong("id"));
        order.setOrderCode(rs.getString("order_code"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerPhone(rs.getString("customer_phone"));
        order.setNote(rs.getString("note"));

        // ---- status (enum-safe) ----
        order.setStatus(rs.getString("status"));

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
                        o.id,
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
                        pu.fullname     AS processed_user_name
                    FROM orders o
                    JOIN users cu ON o.created_by = cu.id
                    LEFT JOIN users pu ON o.processed_by = pu.id
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
                order.setId(rs.getLong("id"));
                order.setOrderCode(rs.getString("order_code"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerPhone(rs.getString("customer_phone"));
                order.setNote(rs.getString("note"));
                order.setStatus(rs.getString("status"));
                order.setTotal(rs.getBigDecimal("total"));

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

                return order;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order with id: " + orderId, e);
        }
    }


    public List<Order> findAll() {
        String sql = "SELECT\n" +
                "    o.id,\n" +
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
                "    u.fullname      AS created_user_name\n" +
                "FROM orders o\n" +
                "JOIN users u ON o.created_by = u.id\n" +
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
                        o.id,
                        o.order_code,
                        o.customer_name,
                        o.customer_phone,
                        o.note,
                        o.status,
                        o.total_price as total,
                        o.order_date,
                        o.processed_at,
                
                        u.id AS created_user_id,
                        u.fullname AS created_user_name
                    FROM orders o
                    JOIN users u ON o.created_by = u.id
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
                        o.id,
                        o.order_code,
                        o.customer_name,
                        o.customer_phone,
                        o.note,
                        o.status,
                        o.total_price as total,
                        o.order_date,
                        o.processed_at,
                
                        u.id AS created_user_id,
                        u.fullname AS created_user_name
                    FROM orders o
                    JOIN users u ON o.created_by = u.id
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

    /**
     * Recalculate order totals after item changes.
     */
    public void refreshOrderFinalTotal(Long orderId) {
        String subtotalSql = "SELECT COALESCE(SUM(quantity * price_at_purchase), 0) AS subtotal FROM order_items WHERE order_id = ?";
        String updateSql = "UPDATE orders SET total_price = ?, final_total = ? WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            BigDecimal subtotal = BigDecimal.ZERO;

            try (PreparedStatement ps = con.prepareStatement(subtotalSql)) {
                ps.setLong(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getBigDecimal("subtotal") != null) {
                        subtotal = rs.getBigDecimal("subtotal");
                    }
                }
            }

            subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);

            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setBigDecimal(1, subtotal);
                ps.setBigDecimal(2, subtotal);
                ps.setLong(3, orderId);

                int affected = ps.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Order not found with ID: " + orderId);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to refresh order final total", e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to refresh order final total", e);
        }
    }

    public boolean updateStatus(String orderCode, String status) {

        StringBuilder sql = new StringBuilder("update orders set status = ? where order_code = ?");

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(1, status);
            }

            if (orderCode != null && !orderCode.trim().isEmpty()) {
                ps.setString(2, orderCode);
            }

            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order status", e);
        }
    }

    public int countOrdersByRole(Long userId, String roleName, String status, String searchKeyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders o WHERE 1=1");

        if ("Salesman".equalsIgnoreCase(roleName)) {
            sql.append(" AND o.created_by = ?");
        } else {
            sql.append(" AND o.status <> 'DRAFT'");
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND o.status = ?");
        }

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            sql.append(" AND (o.order_code LIKE ? OR o.customer_name LIKE ? OR o.customer_phone LIKE ?)");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if ("Salesman".equalsIgnoreCase(roleName)) {
                if (userId == null) {
                    return 0;
                }
                ps.setLong(paramIndex++, userId);
            }

            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count orders by role", e);
        }
    }

    public List<Order> searchOrdersByRole(Long userId, String roleName, String status, String searchKeyword,
                                          String sortBy, String sortDir, int offset, int limit) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.*, u.fullname " +
                        "FROM orders o " +
                        "LEFT JOIN users u ON o.created_by = u.id " +
                        "WHERE 1=1"
        );

        if ("Salesman".equalsIgnoreCase(roleName)) {
            sql.append(" AND o.created_by = ?");
        } else {
            sql.append(" AND o.status <> 'DRAFT'");
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND o.status = ?");
        }

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            sql.append(" AND (o.order_code LIKE ? OR o.customer_name LIKE ? OR o.customer_phone LIKE ?)");
        }

        String orderBy = "o.order_date";
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "orderCode":
                    orderBy = "o.order_code";
                    break;
                case "customerName":
                    orderBy = "o.customer_name";
                    break;
                case "status":
                    orderBy = "o.status";
                    break;
                case "createdAt":
                    orderBy = "o.order_date";
                    break;
                default:
                    orderBy = "o.order_date";
            }
        }

        String direction = "DESC";
        if (sortDir != null && sortDir.equalsIgnoreCase("asc")) {
            direction = "ASC";
        }

        sql.append(" ORDER BY ").append(orderBy).append(" ").append(direction).append(" LIMIT ? OFFSET ?");

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if ("Salesman".equalsIgnoreCase(roleName)) {
                if (userId == null) {
                    return orders;
                }
                ps.setLong(paramIndex++, userId);
            }

            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
            }

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex++, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setOrderCode(rs.getString("order_code"));
                    order.setCustomerName(rs.getString("customer_name"));
                    order.setCustomerPhone(rs.getString("customer_phone"));
                    order.setStatus(rs.getString("status"));
                    order.setNote(rs.getString("note"));
                    order.setCreatedAt(rs.getTimestamp("order_date"));

                    User creator = new User();
                    creator.setId(rs.getLong("created_by"));
                    creator.setFullName(rs.getString("fullname"));
                    order.setCreatedBy(creator);

                    orders.add(order);
                }
            }

            return orders;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search orders by role", e);
        }
    }

    //for pagination
    public int countOrders(String status, String searchCode) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders WHERE status not like 'DRAFT'");

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }

        if (searchCode != null && !searchCode.isEmpty()) {
            sql.append(" AND order_code LIKE ?");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            if (searchCode != null && !searchCode.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchCode + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count orders", e);
        }
    }

    public List<Order> getOrders(String status, String searchCode, String sortBy, String sortDir, int offset, int limit) {
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM orders o WHERE o.status not like 'DRAFT'"
        );

        if (status != null && !status.isEmpty()) {
            sql.append(" AND o.status = ?");
        }

        if (searchCode != null && !searchCode.isEmpty()) {
            sql.append(" AND o.order_code LIKE ?");
        }

        // Add sorting
        String orderBy = "o.order_date";
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "orderCode":
                    orderBy = "o.order_code";
                    break;
                case "customerName":
                    orderBy = "o.customer_name";
                    break;
                case "status":
                    orderBy = "o.status";
                    break;
                case "createdAt":
                    orderBy = "o.order_date";
                    break;
                default:
                    orderBy = "o.order_date";
            }
        }

        String direction = "DESC";
        if (sortDir != null && sortDir.equalsIgnoreCase("asc")) {
            direction = "ASC";
        }

        sql.append(" ORDER BY ").append(orderBy).append(" ").append(direction).append(" LIMIT ? OFFSET ?");

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            if (searchCode != null && !searchCode.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchCode + "%");
            }

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex++, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setOrderCode(rs.getString("order_code"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("order_date"));
                orders.add(order);
            }

            return orders;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get orders", e);
        }
    }

    public int countOrdersBySalesman(Long salesmanId, String status, String searchCode) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM orders WHERE created_by = ?"
        );

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }

        if (searchCode != null && !searchCode.isEmpty()) {
            sql.append(" AND order_code LIKE ?");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setLong(paramIndex++, salesmanId);

            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            if (searchCode != null && !searchCode.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchCode + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count orders for salesman", e);
        }
    }

    public List<Order> getOrdersBySalesman(Long salesmanId, String status, String searchCode,
                                           String sortBy, String sortDir, int offset, int limit) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.*, u.fullname " +
                        "FROM orders o " +
                        "LEFT JOIN users u ON o.created_by = u.id " +
                        "WHERE o.created_by = ?"
        );

        if (status != null && !status.isEmpty()) {
            sql.append(" AND o.status = ?");
        }

        if (searchCode != null && !searchCode.isEmpty()) {
            sql.append(" AND o.order_code LIKE ?");
        }

        // Add sorting
        String orderBy = "o.order_date";
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "orderCode":
                    orderBy = "o.order_code";
                    break;
                case "customerName":
                    orderBy = "o.customer_name";
                    break;
                case "status":
                    orderBy = "o.status";
                    break;
                case "createdAt":
                    orderBy = "o.order_date";
                    break;
                default:
                    orderBy = "o.order_date";
            }
        }

        String direction = "DESC";
        if (sortDir != null && sortDir.equalsIgnoreCase("asc")) {
            direction = "ASC";
        }

        sql.append(" ORDER BY ").append(orderBy).append(" ").append(direction).append(" LIMIT ? OFFSET ?");

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setLong(paramIndex++, salesmanId);

            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            if (searchCode != null && !searchCode.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchCode + "%");
            }

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex++, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setOrderCode(rs.getString("order_code"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerPhone(rs.getString("customer_phone"));
                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));
                order.setCreatedAt(rs.getTimestamp("order_date"));

                // Set creator info
                User creator = new User();
                creator.setId(rs.getLong("created_by"));
                creator.setFullName(rs.getString("fullname"));
                order.setCreatedBy(creator);

                orders.add(order);
            }

            return orders;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get orders for salesman", e);
        }
    }

    public int countByDateRange(Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_date BETWEEN ? AND ?";
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, startDate);
            ps.setTimestamp(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count orders by date range", e);
        }
        return 0;
    }

    public int countBySalesmanAndDateRange(Long salesmanId, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT COUNT(*) FROM orders WHERE created_by = ? AND order_date BETWEEN ? AND ?";
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, salesmanId);
            ps.setTimestamp(2, startDate);
            ps.setTimestamp(3, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count salesman orders by date range", e);
        }
        return 0;
    }

    public int countBySalesmanAndStatus(Long salesmanId, String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE created_by = ? AND status = ?";
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, salesmanId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count salesman orders by status", e);
        }
        return 0;
    }

    public List<Order> findBySalesmanAndDateRange(Long salesmanId, Timestamp startDate, Timestamp endDate) {
        String sql = """
                SELECT
                    o.id,
                    o.order_code,
                    o.customer_name,
                    o.customer_phone,
                    o.note,
                    o.status,
                    o.total_price as total,
                    o.order_date,
                    o.processed_at,
                
                    u.id AS created_user_id,
                    u.fullname AS created_user_name
                FROM orders o
                JOIN users u ON o.created_by = u.id
                WHERE o.created_by = ? AND o.order_date BETWEEN ? AND ?
                ORDER BY o.order_date DESC
                """;
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, salesmanId);
            ps.setTimestamp(2, startDate);
            ps.setTimestamp(3, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find salesman orders by date range", e);
        }
        return orders;
    }

    public List<Order> findRecentBySalesman(Long salesmanId, Timestamp since, int limit) {
        String sql = """
                SELECT
                    o.id,
                    o.order_code,
                    o.customer_name,
                    o.customer_phone,
                    o.note,
                    o.status,
                    o.total_price as total,
                    o.order_date,
                    o.processed_at,
                
                    u.id AS created_user_id,
                    u.fullname AS created_user_name
                FROM orders o
                JOIN users u ON o.created_by = u.id
                WHERE o.created_by = ? AND o.order_date >= ?
                ORDER BY o.order_date DESC LIMIT ?
                """;
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, salesmanId);
            ps.setTimestamp(2, since);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find recent salesman orders", e);
        }
        return orders;
    }

    public List<Order> findRecentOrders(int limit) {
        String sql = """
                SELECT
                    o.id,
                    o.order_code,
                    o.customer_name,
                    o.customer_phone,
                    o.note,
                    o.status,
                    o.processed_at,
                    o.total_price as total,
                    o.order_date,
                
                    u.id AS created_user_id,
                    u.fullname AS created_user_name
                FROM orders o
                LEFT JOIN users u ON o.created_by = u.id
                ORDER BY o.order_date DESC LIMIT ?
                """;
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    User creator = new User();
                    creator.setId(rs.getLong("created_user_id"));
                    creator.setFullName(rs.getString("created_user_name"));
                    order.setCreatedBy(creator);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find recent orders", e);
        }
        return orders;
    }

    public int countByStatusAndDateRange(String status, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ? AND order_date BETWEEN ? AND ?";
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, startDate);
            ps.setTimestamp(3, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count orders by status and date range", e);
        }
        return 0;
    }

    public int countExportHistory(String searchCode, LocalDate fromDate, LocalDate toDate) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders WHERE status = 'COMPLETED'");

        if (searchCode != null && !searchCode.isEmpty()) {
            sql.append(" AND order_code LIKE ?");
        }
        if (fromDate != null) {
            sql.append(" AND CAST(processed_at AS DATE) >= ?");
        }
        if (toDate != null) {
            sql.append(" AND CAST(processed_at AS DATE) <= ?");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (searchCode != null && !searchCode.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchCode + "%");
            }
            if (fromDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(fromDate));
            }
            if (toDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(toDate));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count export history orders", e);
        }
        return 0;
    }

    public List<Order> getExportHistoryOrders(String searchCode, LocalDate fromDate, LocalDate toDate, int offset) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.*, u.fullname AS created_user_name, pu.fullname AS processed_user_name " +
                        "FROM orders o " +
                        "LEFT JOIN users u ON o.created_by = u.id " +
                        "LEFT JOIN users pu ON o.processed_by = pu.id " +
                        "WHERE o.status = 'COMPLETED'"
        );

        if (searchCode != null && !searchCode.isEmpty()) {
            sql.append(" AND o.order_code LIKE ?");
        }
        if (fromDate != null) {
            sql.append(" AND CAST(o.processed_at AS DATE) >= ?");
        }
        if (toDate != null) {
            sql.append(" AND CAST(o.processed_at AS DATE) <= ?");
        }

        sql.append(" ORDER BY o.processed_at DESC LIMIT ? OFFSET ?");

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (searchCode != null && !searchCode.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchCode + "%");
            }
            if (fromDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(fromDate));
            }
            if (toDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(toDate));
            }
            int limit = AppConstants.PAGE_SIZE;

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex++, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setOrderCode(rs.getString("order_code"));
                    order.setCustomerName(rs.getString("customer_name"));
                    order.setCustomerPhone(rs.getString("customer_phone"));
                    order.setStatus(rs.getString("status"));
                    order.setTotal(rs.getBigDecimal("total_price"));
                    order.setCreatedAt(rs.getTimestamp("order_date"));
                    order.setProcessedAt(rs.getTimestamp("processed_at"));

                    User processedBy = new User();
                    long processedUserId = rs.getLong("processed_by");
                    if (!rs.wasNull()) {
                        processedBy.setId(processedUserId);
                        processedBy.setFullName(rs.getString("processed_user_name"));
                        order.setProcessedBy(processedBy);
                    }
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get export history orders", e);
        }
        return orders;
    }

    public ExportDTO getExportOrderHeader(Long orderId) {
        String sql = """
                    select o.order_code, o.customer_name, o.total_price, o.note, o.order_date, o.note,
                  o.processed_at, creater.fullname as salesman_name, processer.fullname as warehouse_staff_name
                  from orders o
                  join users creater
                  on creater.id = o.created_by
                  join users processer
                  on processer.id = o.processed_by
                  where o.id = ?;
                """;
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExportDTO dto = new ExportDTO();
                    dto.setOrderCode(rs.getString("order_code"));
                    dto.setCustomerName(rs.getString("customer_name"));
                    dto.setTotal(rs.getDouble("total_price"));
                    dto.setSalesmanName(rs.getString("salesman_name"));
                    dto.setCreatedAt(rs.getTimestamp("order_date").toLocalDateTime());
                    dto.setWarehouseStaffName(rs.getString("warehouse_staff_name"));
                    dto.setProcessedAt(rs.getTimestamp("processed_at").toLocalDateTime());
                    dto.setNote(rs.getString("note"));
                    return dto;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get export order header for order ID: " + orderId, e);
        }
        return null;
    }

    public List<ExportProductDTO> getExportOrderItems(Long orderId) {
        String sql = """
                      select oi.id as item_id, oi.quantity, oi.price_at_purchase, p.name, pi.serial, oi.price_at_purchase, pi.id as product_item_id, u.name as unit
                from orders o
                join order_items oi
                on o.id = oi.order_id
                join order_item_product_items oipi
                on oi.id = oipi.order_item_id
                join product_items pi
                on pi.id = oipi.product_item_id
                join products p\s
                on p.id = pi.product_id
                join units u
                on u.id = p.unit_id
                where oi.order_id = ?;
                """;
        Map<Long, ExportProductDTO> itemMap = new LinkedHashMap<>();
        // key: id of order item
        // value: info of order item and list of product items
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // get id of order item
                    Long itemId = rs.getLong("item_id");
                    ExportProductDTO item = itemMap.get(itemId);
                    // check if order item already exist in maps
                    if (item == null) {
                        item = new ExportProductDTO();
                        item.setId(itemId);
                        item.setProductName(rs.getString("name"));
                        item.setQuantity(rs.getInt("quantity"));
                        item.setPriceAtPurchase(rs.getDouble("price_at_purchase"));
                        item.setUnit(rs.getString("unit"));
                        item.setProductItems(new ArrayList<>());
                        itemMap.put(itemId, item);
                    }

                    // get product item info and add to order item
                    ExportProductItemDTO pi = new ExportProductItemDTO();
                    pi.setId(rs.getLong("product_item_id"));
                    pi.setSerial(rs.getString("serial"));
                    item.getProductItems().add(pi);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get export order items for order ID: " + orderId, e);
        }
        return new ArrayList<>(itemMap.values());
    }
}
