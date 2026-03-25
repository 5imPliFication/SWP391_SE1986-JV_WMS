package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportProductOrderDTO {
    private Long orderId;
    private String orderCode;
    private String customerName;
    private Timestamp processedAt;
    private String processedByName;
    private long quantity;
}
