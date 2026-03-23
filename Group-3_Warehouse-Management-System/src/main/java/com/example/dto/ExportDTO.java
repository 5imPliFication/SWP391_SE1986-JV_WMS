package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportDTO {
    private Long id;
    private String orderCode;
    private String customerName;
    private Double total;
    private String salesmanName;
    private LocalDateTime createdAt;
    private String warehouseStaffName;
    private LocalDateTime processedAt;
    private String note;
    private List<ExportProductDTO> items;
}
