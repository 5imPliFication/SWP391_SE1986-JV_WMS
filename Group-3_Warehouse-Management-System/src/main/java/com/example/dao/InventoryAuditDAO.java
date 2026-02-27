package com.example.dao;

import com.example.config.DBConfig;
import com.example.enums.InventoryAuditStatus;
import com.example.model.*;
import com.example.util.AppConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryAuditDAO {

    protected LocalDateTime getLocalDateTime(ResultSet rs, String column)
            throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts == null ? null : ts.toLocalDateTime();
    }

    public List<InventoryAudit> getAll(String auditCode, String status, int pageNo) {

        List<InventoryAudit> inventoryAudits = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                    SELECT i.id, i.audit_code, u.id as user_id, u.fullname, i.status, i.created_at, i.updated_at
                    FROM inventory_audits i
                    JOIN users u ON i.created_by = u.id
                    WHERE 1=1
                """);

        if (auditCode != null && !auditCode.trim().isEmpty()) {
            sql.append(" AND i.audit_code LIKE ? ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND i.status = ? ");
        }

        sql.append(" ORDER BY i.created_at DESC ");
        sql.append(" LIMIT ? OFFSET ? ");

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (auditCode != null && !auditCode.trim().isEmpty()) {
                ps.setString(index++, "%" + auditCode + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ps.setInt(index++, AppConstants.PAGE_SIZE);
            ps.setInt(index++, (pageNo - 1) * AppConstants.PAGE_SIZE);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InventoryAudit inventoryAudit = new InventoryAudit();
                inventoryAudit.setId(rs.getLong("id"));
                inventoryAudit.setAuditCode(rs.getString("audit_code"));
                inventoryAudit.setStatus(InventoryAuditStatus.valueOf(rs.getString("status")));
                inventoryAudit.setCreatedAt(getLocalDateTime(rs, "created_at"));
                inventoryAudit.setUpdatedAt(getLocalDateTime(rs, "updated_at"));

                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setFullName(rs.getString("fullname"));
                inventoryAudit.setUser(user);

                inventoryAudits.add(inventoryAudit);
            }

            return inventoryAudits;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countInventoryAudits(String auditCode, String status) {

        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(*)
                    FROM inventory_audits i
                    WHERE 1=1
                """);

        if (auditCode != null && !auditCode.trim().isEmpty()) {
            sql.append(" AND i.audit_code LIKE ? ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND i.status = ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (auditCode != null && !auditCode.trim().isEmpty()) {
                ps.setString(index++, "%" + auditCode + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Create new audit
    public long createInventoryAudit(Connection conn, InventoryAudit inventoryAudit) {
        String sql = """
                INSERT INTO inventory_audits (audit_code, created_by, status, created_at, updated_at)
                VALUES (?, ?, ?, NOW(), NOW())
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, inventoryAudit.getAuditCode());
            ps.setLong(2, inventoryAudit.getUser().getId());
            ps.setString(3, inventoryAudit.getStatus().name());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new SQLException("Insert failed, no ID returned.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createInventoryAuditItem(Connection conn, InventoryAuditItem inventoryAuditItem) {
        String sql = """
                INSERT INTO inventory_audit_items (audit_id, product_id, system_quantity)
                VALUES (?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inventoryAuditItem.getAuditId());
            ps.setLong(2, inventoryAuditItem.getProduct().getId());
            ps.setLong(3, inventoryAuditItem.getProduct().getTotalQuantity());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public InventoryAudit findById(Long id) {
        String sql = """
                SELECT i.id, i.audit_code, u.id as user_id, u.fullname, i.status, i.created_at, i.updated_at
                FROM inventory_audits i
                JOIN users u ON i.created_by = u.id
                WHERE i.id = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                InventoryAudit inventoryAudit = new InventoryAudit();
                inventoryAudit.setId(rs.getLong("id"));
                inventoryAudit.setAuditCode(rs.getString("audit_code"));
                inventoryAudit.setStatus(InventoryAuditStatus.valueOf(rs.getString("status")));
                inventoryAudit.setCreatedAt(getLocalDateTime(rs, "created_at"));
                inventoryAudit.setUpdatedAt(getLocalDateTime(rs, "updated_at"));

                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setFullName(rs.getString("fullname"));
                inventoryAudit.setUser(user);

                return inventoryAudit;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InventoryAuditItem> findItemsByAuditId(Long auditId) {
        List<InventoryAuditItem> items = new ArrayList<>();
        String sql = """
                SELECT ai.id, ai.audit_id, ai.product_id, ai.system_quantity, ai.physical_quantity, ai.discrepancy, ai.reason, p.name as product_name
                FROM inventory_audit_items ai
                JOIN products p ON ai.product_id = p.id
                WHERE ai.audit_id = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, auditId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InventoryAuditItem item = new InventoryAuditItem();
                item.setId(rs.getLong("id"));
                item.setAuditId(rs.getLong("audit_id"));

                Product product = new Product();
                product.setId(rs.getLong("product_id"));
                product.setName(rs.getString("product_name"));
                item.setProduct(product);

                item.setSystemQuantity(rs.getLong("system_quantity"));

                long physicalQuantity = rs.getLong("physical_quantity");
                if (rs.wasNull()) {
                    item.setPhysicalQuantity(null); // Gán null thật sự vào object
                } else {
                    item.setPhysicalQuantity(physicalQuantity);
                }

                long discrepancy = rs.getLong("discrepancy");
                if (rs.wasNull()) {
                    item.setDiscrepancy(null); // Gán null thật sự vào object
                } else {
                    item.setDiscrepancy(discrepancy);
                }

                String reason = rs.getString("reason");
                if (rs.wasNull()) {
                    item.setReason(null); // Gán null thật sự vào object
                } else {
                    item.setReason(reason);
                }

                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateInventoryAuditStatus(Long auditId, InventoryAuditStatus status) {
        String sql = """
                UPDATE inventory_audits
                SET status = ?, updated_at = NOW()
                WHERE id = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, auditId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateInventoryAuditItem(Connection conn, Long itemId, Long physicalQuantity, String reason) {
        String sql = """
                UPDATE inventory_audit_items
                SET physical_quantity = ?, reason = ?, discrepancy = system_quantity - ?
                WHERE id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, physicalQuantity);
            ps.setString(2, reason);
            ps.setLong(3, physicalQuantity);
            ps.setLong(4, itemId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}