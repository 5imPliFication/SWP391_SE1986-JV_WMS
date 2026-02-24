package com.example.model;

import com.example.enums.InventoryAuditStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryAudit {
    private Long id;
    private String auditCode;
    private User user;
    private InventoryAuditStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<InventoryAuditItem> inventoryAuditItems;
}