package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryReportDTO {

    private String productName;
    private long openingStock;
    private long totalImport;
    private long totalExport;
    private long closingStock;
}
