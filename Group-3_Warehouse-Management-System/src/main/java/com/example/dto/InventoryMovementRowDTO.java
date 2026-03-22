package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovementRowDTO {
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private long openingQty;
    private long importQty;
    private long exportQty;
    private long closingQty;
    private BigDecimal openingValue;
    private BigDecimal importValue;
    private BigDecimal exportValue;
    private BigDecimal closingValue;
}