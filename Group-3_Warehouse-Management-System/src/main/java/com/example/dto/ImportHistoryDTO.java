package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportHistoryDTO {
    private Long id;
    private String receiptCode;
    private Timestamp receivedAt;
    private String staffName;
    private Long totalQuantity;
    private String note;
}
