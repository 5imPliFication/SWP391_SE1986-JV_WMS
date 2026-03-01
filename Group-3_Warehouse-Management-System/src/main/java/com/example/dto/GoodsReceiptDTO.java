package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoodsReceiptDTO {
    private Long id;
    private String receiptCode;
    private LocalDateTime receivedAt;
    private String warehouseName;
    private String note;
}
