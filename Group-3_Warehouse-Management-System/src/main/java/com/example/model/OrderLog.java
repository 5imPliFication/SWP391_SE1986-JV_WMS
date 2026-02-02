package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLog {
    private Long id;
    private Long orderId;
    private String action;
    private Long performedBy;
    private Timestamp performedAt;
    private String remark;
}
