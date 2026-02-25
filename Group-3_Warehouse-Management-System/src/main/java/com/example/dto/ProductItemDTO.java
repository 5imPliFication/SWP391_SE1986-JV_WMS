package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDTO {
    private Long productId;
    private String productName;
    private String serial;
    private Double importPrice;

    public ProductItemDTO(Long productId, String serial, Double importPrice) {
        this.productId = productId;
        this.serial = serial;
        this.importPrice = importPrice;
    }
}
