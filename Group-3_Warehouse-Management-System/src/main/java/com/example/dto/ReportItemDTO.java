package com.example.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportItemDTO {
    private Long productId;
    private String productName;
    private long quantity;

    public ReportItemDTO(String productName, long quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }
}
