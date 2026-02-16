package com.example.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PurchaseRequest {

    private Long id;
    private String requestCode;
    private String status;
    private String note;
    private LocalDateTime createdAt;

    private Long createdBy;
    private String createdByName;

    private Long approvedBy;
    private String approvedByName;
}
