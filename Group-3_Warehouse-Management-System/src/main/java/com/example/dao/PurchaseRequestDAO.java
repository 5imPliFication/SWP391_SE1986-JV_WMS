/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.PurchaseRequestDTO;
import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Chip;
import com.example.model.Model;
import com.example.model.Product;
import com.example.model.PurchaseRequest;
import com.example.model.PurchaseRequestItem;
import com.example.model.Ram;
import com.example.model.Size;
import com.example.model.Storage;
import com.example.model.Unit;
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
                insertExistingProduct(conn, purchaseRequestId, item);
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

    public List<PurchaseRequest> search(
            Long userId,
            String role,
            String requestCode,
            String status,
            String createdDate,
            int pageNo,
            int pageSize
    ) {

        List<PurchaseRequest> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT pr.id, pr.request_code, pr.status, pr.note, pr.created_at,
               u1.fullname AS created_by_name,
               u2.fullname AS approved_by_name
        FROM purchase_requests pr
        JOIN users u1 ON pr.created_by = u1.id
        LEFT JOIN users u2 ON pr.approved_by = u2.id
        WHERE 1 = 1
    """);

        List<Object> params = new ArrayList<>();

        /* ===== ROLE FILTER ===== */
        if ("Salesman".equalsIgnoreCase(role)) {
            sql.append(" AND pr.created_by = ?");
            params.add(userId);
        }

        if ("Warehouse".equalsIgnoreCase(role)) {
            sql.append(" AND pr.status IN ('APPROVED')");
        }

        /* ===== REQUEST CODE ===== */
        if (requestCode != null && !requestCode.isBlank()) {
            sql.append(" AND pr.request_code LIKE ?");
            params.add("%" + requestCode + "%");
        }

        /* ===== STATUS FILTER (KHÔNG ÁP DỤNG CHO WAREHOUSE) ===== */
        if (!"Warehouse".equalsIgnoreCase(role)
                && status != null
                && !status.isBlank()) {

            sql.append(" AND pr.status = ?");
            params.add(status);
        }

        /* ===== CREATED DATE ===== */
        if (createdDate != null && !createdDate.isBlank()) {
            sql.append(" AND DATE(pr.created_at) = ?");
            params.add(createdDate);
        }

        /* ===== PAGINATION ===== */
        sql.append(" ORDER BY pr.created_at DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((pageNo - 1) * pageSize);

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
                pr.setCreatedByName(rs.getString("created_by_name"));
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
            String role,
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

        if ("Salesman".equalsIgnoreCase(role)) {
            sql.append(" AND pr.created_by = ?");
            params.add(userId);
        }

        if ("Warehouse".equalsIgnoreCase(role)) {
            sql.append(" AND pr.status = ?");
            params.add("APPROVED");
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

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public List<Product> getActiveProduct() {

        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT 
            p.id,
            p.name,

            b.name AS brand_name,
            c.name AS category_name,
            u.symbol,

            m.name AS model_name,
            ch.name AS chip_name,
            r.size AS ram_size,
            s.size AS storage_size,
            sz.size AS size_value

        FROM products p

        LEFT JOIN brands b ON p.brand_id = b.id
        LEFT JOIN categories c ON p.category_id = c.id
        LEFT JOIN units u ON p.unit_id = u.id

        LEFT JOIN models m ON p.model_id = m.id
        LEFT JOIN chips ch ON p.chip_id = ch.id
        LEFT JOIN rams r ON p.ram_id = r.id
        LEFT JOIN storages s ON p.storage_id = s.id
        LEFT JOIN sizes sz ON p.size_id = sz.id

        WHERE p.is_active = true
        ORDER BY p.name
    """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Product p = new Product();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));

                Brand brand = new Brand();
                brand.setName(rs.getString("brand_name"));
                p.setBrand(brand);

                Category category = new Category();
                category.setName(rs.getString("category_name"));
                p.setCategory(category);

                Unit unit = new Unit();
                unit.setSymbol(rs.getString("symbol"));
                p.setUnit(unit);

                Model model = new Model();
                model.setName(rs.getString("model_name"));
                p.setModel(model);

                Chip chip = new Chip();
                chip.setName(rs.getString("chip_name"));
                p.setChip(chip);

                Ram ram = new Ram();
                ram.setSize(rs.getString("ram_size"));
                p.setRam(ram);

                Storage storage = new Storage();
                storage.setSize(rs.getString("storage_size"));
                p.setStorage(storage);

                Size size = new Size();
                size.setSize(rs.getString("size_value"));
                p.setSize(size);

                list.add(p);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public PurchaseRequest findById(
            Long requestId,
            Long userId,
            String role
    ) {

        StringBuilder sql = new StringBuilder("""
        SELECT pr.id, pr.request_code, pr.status, pr.note, pr.created_at,
               u1.id AS created_by, u1.fullname AS created_by_name,
               u2.id AS approved_by, u2.fullname AS approved_by_name
        FROM purchase_requests pr
        JOIN users u1 ON pr.created_by = u1.id
        LEFT JOIN users u2 ON pr.approved_by = u2.id
        WHERE pr.id = ?
    """);

        List<Object> params = new ArrayList<>();
        params.add(requestId);

        // ===== ROLE RULE =====
        if ("STAFF".equalsIgnoreCase(role)) {
            sql.append(" AND pr.created_by = ?");
            params.add(userId);
        }

        if ("WAREHOUSE".equalsIgnoreCase(role)) {
            sql.append(" AND pr.status = 'APPROVED'");
        }

        // MANAGER → không thêm điều kiện
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
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
                p.name AS product_name,
                pri.quantity,
                u.symbol AS unit,
                b.name AS brand_name,
                c.name AS category_name
            FROM purchase_request_items pri
            JOIN products p ON pri.product_id = p.id
            LEFT JOIN units u ON p.unit_id = u.id
            LEFT JOIN brands b ON p.brand_id = b.id
            LEFT JOIN categories c ON p.category_id = c.id
            WHERE pri.purchase_request_id = ?;
    """;

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, requestId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseRequestItem item = new PurchaseRequestItem();

                Long productId = rs.getLong("product_id");
                if (rs.wasNull()) {
                    productId = null;
                }

                item.setProductId(productId);
                item.setProductName(rs.getString("product_name"));
                item.setBrandName(rs.getString("brand_name"));
                item.setCategoryName(rs.getString("category_name"));
                item.setQuantity(rs.getLong("quantity"));
                item.setUnit(rs.getString("unit"));

                items.add(item);
            }

        } catch (Exception e) {
            throw new RuntimeException("Find purchase request items failed", e);
        }

        return items;
    }

    // Dành cho MANAGER
    public boolean updateStatusByManager(Long prId, String status, Long managerId) {
        String sql = """
        UPDATE purchase_requests
        SET status = ?, approved_by = ?
        WHERE id = ?
    """;

        try (Connection con = DBConfig.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setLong(2, managerId);
            ps.setLong(3, prId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateStatus(Long prId, String status) {
        String sql = """
        UPDATE purchase_requests
        SET status = ?
        WHERE id = ?
    """;

        try (Connection con = DBConfig.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setLong(2, prId);

            return ps.executeUpdate() > 0;
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

    public void updateItems(Long requestId, List<PurchaseRequestItem> items) {

        String deleteSql = """
            DELETE FROM purchase_request_items
            WHERE purchase_request_id = ?
        """;

        String insertSql = """
            INSERT INTO purchase_request_items
            (purchase_request_id, product_id, quantity)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setLong(1, requestId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {

                for (PurchaseRequestItem i : items) {
                    ps.setLong(1, requestId);
                    ps.setLong(2, i.getProductId());
                    ps.setLong(3, i.getQuantity());
                    ps.addBatch();
                }

                ps.executeBatch();
            }

        } catch (Exception e) {
            throw new RuntimeException("Update purchase request items failed", e);
        }
    }

    public void updateNote(Long requestId, String note) {

        String sql = """
            UPDATE purchase_requests
            SET note = ?
            WHERE id = ? AND status = 'PENDING'
        """;

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, note);
            ps.setLong(2, requestId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Update purchase request failed", e);
        }
    }

    public PurchaseRequestDTO findPurchaseById(Long purchaseId) {
        StringBuilder sql = new StringBuilder("select * from purchase_requests where 1 = 1 ");

        if (purchaseId != null) {
            sql.append(" and id = ?");
        }
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (purchaseId != null) {
                ps.setLong(1, purchaseId);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
                purchaseRequestDTO.setPurchaseId(rs.getLong("id"));
                purchaseRequestDTO.setPurchaseCode(rs.getString("request_code"));
                purchaseRequestDTO.setNote(rs.getString("note"));
                return purchaseRequestDTO;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
