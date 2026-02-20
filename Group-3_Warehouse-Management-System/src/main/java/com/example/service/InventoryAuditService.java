package com.example.service;

import com.example.dao.InventoryAuditDAO;
import com.example.model.*;

import java.util.List;

public class InventoryAuditService {
    private InventoryAuditDAO inventoryAuditDAO = new InventoryAuditDAO();

    public List<InventoryAudit> findAll(String auditCode, String status, int pageNo) {
        return inventoryAuditDAO.getAll(auditCode, status, pageNo);
    }

    public int getTotalInventoryAudits(String auditCode, String status) {
        return inventoryAuditDAO.countInventoryAudits(auditCode, status);
    }

}