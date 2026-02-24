package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryAuditItem {
    private Long id;
    private Long auditId;
    private Product product;
    private Long systemQuantity;
    private Long physicalQuantity;
    private Long discrepancy;
    private String reason;
}
