package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.InventoryAuditDAO;
import com.example.enums.InventoryAuditStatus;
import com.example.model.*;
import com.example.util.AuditCodeGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InventoryAuditService {
    private InventoryAuditDAO inventoryAuditDAO = new InventoryAuditDAO();

    public List<InventoryAudit> findAll(String auditCode, String status, int pageNo) {
        return inventoryAuditDAO.getAll(auditCode, status, pageNo);
    }

    public int getTotalInventoryAudits(String auditCode, String status) {
        return inventoryAuditDAO.countInventoryAudits(auditCode, status);
    }

    public void createFullAudit(Long createdBy, List<InventoryAuditItem> inventoryAuditItems) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // Create InventoryAudit
            String auditCode = AuditCodeGenerator.generateDailyCode();
            String status = String.valueOf(InventoryAuditStatus.PENDING);
            InventoryAudit inventoryAudit = new InventoryAudit();
            User user = new User();
            user.setId(createdBy);
            inventoryAudit.setAuditCode(auditCode);
            inventoryAudit.setUser(user);
            inventoryAudit.setStatus(InventoryAuditStatus.valueOf(status));

            long auditId = inventoryAuditDAO.createInventoryAudit(conn, inventoryAudit);

            // Create InventoryAuditItems
            for (InventoryAuditItem inventoryAuditItem : inventoryAuditItems) {
                inventoryAuditItem.setAuditId(auditId);
                inventoryAuditDAO.createInventoryAuditItem(conn, inventoryAuditItem);
            }

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }
    }

    public InventoryAudit getInventoryAuditById(Long auditId) {
        return inventoryAuditDAO.findById(auditId);
    }

    public List<InventoryAuditItem> getInventoryAuditItemsByAuditId(Long auditId) {
        return inventoryAuditDAO.findItemsByAuditId(auditId);
    }

    // Manager can change PENDING -> CANCELLED
    public boolean cancelInventoryAudit(Long auditId) {
        return inventoryAuditDAO.updateInventoryAuditStatus(auditId, InventoryAuditStatus.CANCELLED);
    }

    // Save loop --> need Transaction, if any error -> rollback
    public void performInventoryAudit(Long auditId, String[] inventoryAuditItemIds, String[] physicalQuantities, String[] reasons) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            for (int i = 0; i < inventoryAuditItemIds.length; i++) {
                Long itemId = Long.parseLong(inventoryAuditItemIds[i]);
                Long physicalQty = Long.parseLong(physicalQuantities[i]);
                String reasonText = reasons[i];

                inventoryAuditDAO.updateInventoryAuditItem(conn, itemId, physicalQty, reasonText);
            }

            // Update status of InventoryAudit to COMPLETED
            inventoryAuditDAO.updateInventoryAuditStatus(auditId, InventoryAuditStatus.COMPLETED);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}