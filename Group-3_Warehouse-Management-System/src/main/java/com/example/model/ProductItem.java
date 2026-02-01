package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    private Long id;
    private String serial;
    private Double importPrice;
    private LocalDateTime importDate;
    private Boolean isActive;
    private Long productId;

    public ProductItem(String serial, Double importPrice, LocalDateTime importDate, Long productId) {
        this.serial = serial;
        this.importPrice = importPrice;
        this.productId = productId;
        this.importDate = importDate;
    }
}