package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovementRowDTO {
    private Long productId;
    private String productName;
    private long openingQty;
    private long importQty;
    private long exportQty;
    private long closingQty;
}