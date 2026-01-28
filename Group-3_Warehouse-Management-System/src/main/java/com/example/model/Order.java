package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String orderCode;
    private String customerName;
    private String status;
    private Long createdBy;
    private Timestamp createdAt;
    private Long processedBy;
    private Timestamp processedAt;
    private String note;
}
