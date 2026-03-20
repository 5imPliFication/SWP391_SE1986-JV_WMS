package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportItemDTO {
    private Long id;
    private String productName;
    private int quantity;
    private Double priceAtPurchase;
    private String unit;
    private List<ExportProductItemDTO> productItems;
}
