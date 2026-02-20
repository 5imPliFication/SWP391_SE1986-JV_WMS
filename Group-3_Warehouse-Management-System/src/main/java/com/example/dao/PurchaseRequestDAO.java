/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.PurchaseRequest;
import com.example.model.PurchaseRequestItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC
 */
public class PurchaseRequestDAO {

    public long createPurchaseRequest(long createdBy, String note, List<PurchaseRequestItem> items)
            throws SQLException {

        Connection conn = DBConfig.getDataSource().getConnection();
        conn.setAutoCommit(false);

        try {
            // 1. Insert purchase_requests
            String prSql = """
                INSERT INTO purchase_requests
                (request_code, created_by, status, note, created_at)
                VALUES (?, ?, 'PENDING', ?, NOW())
            """;

            PreparedStatement ps = conn.prepareStatement(
                    prSql, Statement.RETURN_GENERATED_KEYS
            );

            String requestCode = "PR-" + System.currentTimeMillis();
            ps.setString(1, requestCode);
            ps.setLong(2, createdBy);
            ps.setString(3, note);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            long purchaseRequestId = rs.getLong(1);

            // 2. Insert items
            for (PurchaseRequestItem item : items) {
                if (item.getProductId() != null) {
                    insertExistingProduct(conn, purchaseRequestId, item);
                } else {
                    insertNewProductProposal(conn, purchaseRequestId, item);
                }
            }

            conn.commit();
            return purchaseRequestId;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    private void insertExistingProduct(
            Connection conn,
            long prId,
            PurchaseRequestItem item
    ) throws SQLException {

        String sql = """
            INSERT INTO purchase_request_items
            (purchase_request_id, product_id, quantity)
            VALUES (?, ?, ?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, prId);
        ps.setLong(2, item.getProductId());
        ps.setLong(3, item.getQuantity());
        ps.executeUpdate();
    }

    private void insertNewProductProposal(
            Connection conn,
            long prId,
            PurchaseRequestItem item
    ) throws SQLException {

        String sql = """
            INSERT INTO purchase_request_items
            (purchase_request_id, product_name, brand_name,
             category_name, specs, quantity)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, prId);
        ps.setString(2, item.getProductName());
        ps.setString(3, item.getBrandName());
        ps.setString(4, item.getCategoryName());
        ps.setString(5, item.getSpecs());
        ps.setLong(6, item.getQuantity());
        ps.executeUpdate();
    }

    public List<PurchaseRequest> search(
            Long userId,
            boolean isManager,
            String requestCode,
            String status,
            String createdDate,
            int pageNo,
            int pageSize
    ) {

        List<PurchaseRequest> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT pr.id, pr.request_code, pr.status, pr.note, pr.created_at,
               u1.id AS created_by, u1.fullname AS created_by_name,
               u2.id AS approved_by, u2.fullname AS approved_by_name
        FROM purchase_requests pr
        JOIN users u1 ON pr.created_by = u1.id
        LEFT JOIN users u2 ON pr.approved_by = u2.id
        WHERE 1 = 1
    """);

        List<Object> params = new ArrayList<>();

        /* ===== ROLE FILTER ===== */
        if (!isManager) {
            sql.append(" AND pr.created_by = ?");
            params.add(userId);
        }

        /* ===== REQUEST CODE ===== */
        if (requestCode != null && !requestCode.isBlank()) {
            sql.append(" AND pr.request_code LIKE ?");
            params.add("%" + requestCode + "%");
        }

        /* ===== STATUS ===== */
        if (status != null && !status.isBlank()) {
            sql.append(" AND pr.status = ?");
            params.add(status);
        }

        /* ===== CREATED DATE ===== */
        if (createdDate != null && !createdDate.isBlank()) {
            sql.append(" AND DATE(pr.created_at) = ?");
            params.add(createdDate);
        }

        /* ===== ORDER + PAGINATION ===== */
        sql.append(" ORDER BY pr.created_at DESC");
        sql.append(" LIMIT ? OFFSET ?");

        int offset = (pageNo - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseRequest pr = new PurchaseRequest();
                pr.setId(rs.getLong("id"));
                pr.setRequestCode(rs.getString("request_code"));
                pr.setStatus(rs.getString("status"));
                pr.setNote(rs.getString("note"));
                pr.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                pr.setCreatedBy(rs.getLong("created_by"));
                pr.setCreatedByName(rs.getString("created_by_name"));

                pr.setApprovedBy(rs.getLong("approved_by"));
                pr.setApprovedByName(rs.getString("approved_by_name"));

                list.add(pr);
            }

        } catch (Exception e) {
            throw new RuntimeException("Search purchase request failed", e);
        }

        return list;
    }

    public int count(
            Long userId,
            boolean isManager,
            String requestCode,
            String status,
            String createdDate
    ) {

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*)
        FROM purchase_requests pr
        WHERE 1 = 1
    """);

        List<Object> params = new ArrayList<>();

        if (!isManager) {
            sql.append(" AND pr.created_by = ?");
            params.add(userId);
        }

        if (requestCode != null && !requestCode.isBlank()) {
            sql.append(" AND pr.request_code LIKE ?");
            params.add("%" + requestCode + "%");
        }

        if (status != null && !status.isBlank()) {
            sql.append(" AND pr.status = ?");
            params.add(status);
        }

        if (createdDate != null && !createdDate.isBlank()) {
            sql.append(" AND DATE(pr.created_at) = ?");
            params.add(createdDate);
        }

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException("Count purchase request failed", e);
        }

        return 0;
    }

    public List<Product> getActiveProductDropdown() {

        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT id, name
        FROM products
        WHERE is_active = true
        ORDER BY name
    """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                list.add(p);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Brand> getActiveBrands() {
        List<Brand> list = new ArrayList<>();

        String sql = "SELECT id, name FROM brands ORDER BY name";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Brand b = new Brand();
                b.setId(rs.getLong("id"));
                b.setName(rs.getString("name"));
                list.add(b);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Category> getActiveCategories() {
        List<Category> list = new ArrayList<>();

        String sql = "SELECT id, name FROM categories ORDER BY name";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getLong("id"));
                c.setName(rs.getString("name"));
                list.add(c);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public PurchaseRequest findById(
            Long requestId,
            Long userId,
            boolean isManager
    ) {

        String sql = """
        SELECT pr.id, pr.request_code, pr.status, pr.note, pr.created_at,
               u1.id AS created_by, u1.fullname AS created_by_name,
               u2.id AS approved_by, u2.fullname AS approved_by_name
        FROM purchase_requests pr
        JOIN users u1 ON pr.created_by = u1.id
        LEFT JOIN users u2 ON pr.approved_by = u2.id
        WHERE pr.id = ?
    """;

        if (!isManager) {
            sql += " AND pr.created_by = ?";
        }

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, requestId);
            if (!isManager) {
                ps.setLong(2, userId);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PurchaseRequest pr = new PurchaseRequest();
                pr.setId(rs.getLong("id"));
                pr.setRequestCode(rs.getString("request_code"));
                pr.setStatus(rs.getString("status"));
                pr.setNote(rs.getString("note"));
                pr.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                pr.setCreatedBy(rs.getLong("created_by"));
                pr.setCreatedByName(rs.getString("created_by_name"));

                pr.setApprovedBy(rs.getLong("approved_by"));
                pr.setApprovedByName(rs.getString("approved_by_name"));

                return pr;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Find purchase request by id failed", e);
        }
    }

    public List<PurchaseRequestItem> findItemsByRequestId(Long requestId) {

        List<PurchaseRequestItem> items = new ArrayList<>();

        String sql = """
        SELECT
            pri.product_id,
            COALESCE(p.name, pri.product_name) AS product_name,
            b.name AS brand_name,
            c.name AS category_name,
            pri.specs,
            pri.quantity
        FROM purchase_request_items pri
        LEFT JOIN products p ON pri.product_id = p.id
        LEFT JOIN brands b ON p.brand_id = b.id
        LEFT JOIN categories c ON p.category_id = c.id
        WHERE pri.purchase_request_id = ?
    """;

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, requestId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseRequestItem item = new PurchaseRequestItem();

                item.setProductId(rs.getLong("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setBrandName(rs.getString("brand_name"));
                item.setCategoryName(rs.getString("category_name"));
                item.setSpecs(rs.getString("specs"));
                item.setQuantity(rs.getLong("quantity"));

                items.add(item);
            }

        } catch (Exception e) {
            throw new RuntimeException("Find purchase request items failed", e);
        }

        return items;
    }

    public void cancel(Long prId, Long userId) {
        String sql = """
        UPDATE purchase_requests
        SET status = 'CANCELLED'
        WHERE id = ?
          AND created_by = ?
          AND status = 'PENDING'
    """;

        try (Connection con = DBConfig.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, prId);
            ps.setLong(2, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStatusById(Long prId) {

        String sql = """
        SELECT status
        FROM purchase_requests
        WHERE id = ?
    """;

        try (Connection con = DBConfig.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, prId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Cannot get purchase request status", e);
        }

        return null; // không tìm thấy
    }

}
