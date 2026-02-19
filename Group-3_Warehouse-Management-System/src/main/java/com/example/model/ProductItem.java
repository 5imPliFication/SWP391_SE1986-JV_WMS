package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    private Long id;
    private String serial;
    private Double importedPrice;
    private Double currentPrice;
    private LocalDateTime importedAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private Long productId;

    public ProductItem(String serial, Double importedPrice, LocalDateTime importedAt, Long productId) {
        this.serial = serial;
        this.importedPrice = importedPrice;
        this.productId = productId;
        this.importedAt = importedAt;
    }
}