package com.example.service;

import com.example.dao.PurchaseRequestDAO;
import com.example.dto.PurchaseRequestDTO;
import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.PurchaseRequest;
import com.example.model.PurchaseRequestItem;
import com.example.model.User;

import java.sql.SQLException;
import java.util.List;

public class PurchaseRequestService {

    private final PurchaseRequestDAO p;

    public PurchaseRequestService() {
        p = new PurchaseRequestDAO();
    }

    public List<PurchaseRequest> getList(
            Long userId,
            String role,
            String requestCode,
            String status,
            String createdDate,
            int pageNo,
            int pageSize
    ) {
        return p.search(userId, role, requestCode, status, createdDate, pageNo, pageSize);
    }

    public int count(
            Long userId,
            String role,
            String requestCode,
            String status,
            String createdDate
    ) {
        return p.count(userId, role, requestCode, status, createdDate);
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

    public List<Product> getProductDropdown() {
        return p.getActiveProductDropdown();
    }

    public List<Brand> getBrandsDropdown() {
        return p.getActiveBrands();
    }

    public List<Category> getCategoryDropdown() {
        return p.getActiveCategories();
    }

    public PurchaseRequest getDetail(Long requestId, User user) {

        String role = user.getRole().getName();

        PurchaseRequest pr = p.findById(
                requestId,
                user.getId(),
                role
        );

        if (pr == null) {
            throw new RuntimeException("Purchase request not found or access denied");
        }

        return pr;
    }

    public List<PurchaseRequestItem> getItems(Long requestId) {
        return p.findItemsByRequestId(requestId);
    }

    public String getStatus(Long prId) {

        String status = p.getStatusById(prId);

        if (status == null) {
            throw new RuntimeException("Purchase Request not found");
        }

        return status;
    }

    public void cancel(Long id) {
        if (!p.updateStatus(id, "CANCELLED")) {
            throw new RuntimeException("Cancel failed");
        }
    }

    public void approve(Long id, Long mID) {
        if (!p.updateStatusByManager(id, "APPROVED", mID)) {
            throw new RuntimeException("Approve failed");
        }
    }

    public void reject(Long id, Long mID) {
        if (!p.updateStatusByManager(id, "REJECTED", mID)) {
            throw new RuntimeException("Reject failed");
        }
    }

    public void complete(Long id) {
        if (!p.updateStatus(id, "COMPLETED")) {
            throw new RuntimeException("complete failed");
        }
    }

    public void updatePurchaseRequest(
            Long requestId,
            String note,
            List<PurchaseRequestItem> items,
            User user
    ) {
        // (Optional) check quyền
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }

        // Update header
        p.updateNote(requestId, note);

        // Update items
        p.updateItems(requestId, items);
    }

    public PurchaseRequestDTO findPurchaseById(Long purchaseId) {
        return p.findPurchaseById(purchaseId);
    }
}
