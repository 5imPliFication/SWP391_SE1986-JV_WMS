package com.example.service;

import com.example.dao.PurchaseRequestDAO;
import com.example.model.PurchaseRequest;
import com.example.model.PurchaseRequestItem;

import java.sql.SQLException;
import java.util.List;

public class PurchaseRequestService {

    private final PurchaseRequestDAO p;

    public PurchaseRequestService() {
        p = new PurchaseRequestDAO();
    }

    public List<PurchaseRequest> getList(Long userId, String roleName) {
        boolean isManager = "Manager".equalsIgnoreCase(roleName);
        return p.findAll(userId, isManager);
    }

    public long createPurchaseRequest(
            long createdBy,
            String note,
            List<PurchaseRequestItem> items
    ) {

        // =========================
        // Business validation
        // =========================
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Purchase request must have at least one item");
        }

        for (PurchaseRequestItem item : items) {

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            // Trường hợp sản phẩm đã tồn tại
            if (item.getProductId() != null) {
                continue;
            }

            // Trường hợp sản phẩm chưa tồn tại (proposal)
            if (item.getProductName() == null || item.getProductName().isBlank()) {
                throw new IllegalArgumentException("New product must have product name");
            }

            if (item.getBrandName() == null || item.getBrandName().isBlank()) {
                throw new IllegalArgumentException("New product must have brand name");
            }

            if (item.getCategoryName() == null || item.getCategoryName().isBlank()) {
                throw new IllegalArgumentException("New product must have category name");
            }
        }
        try {
            return p.createPurchaseRequest(
                    createdBy,
                    note,
                    items
            );
        } catch (SQLException e) {
            throw new RuntimeException("Create purchase request failed", e);
        }
    }
}
