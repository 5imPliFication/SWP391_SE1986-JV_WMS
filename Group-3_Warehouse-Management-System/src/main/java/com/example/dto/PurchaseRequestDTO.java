package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDTO {
    private Long purchaseId;
    private String purchaseCode;
    private String note;
    private String createdBy;
    private LocalDateTime createdAt;
    private String approvedBy;
    private LocalDateTime approvedAt;
}
