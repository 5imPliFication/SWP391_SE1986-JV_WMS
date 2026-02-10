package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportOrderDTO {
    private Long id;
    private String code;
    private Timestamp exportDate;
    private String salesmanName;
    private String customerName;
    private String status;
}
