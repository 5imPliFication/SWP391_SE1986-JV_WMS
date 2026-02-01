package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportProductItemDTO {
    private Long productId;
    private String productName;
    private String serial;
    private Double importPrice;
}
