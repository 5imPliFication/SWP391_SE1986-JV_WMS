package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoodsReceiptItemDTO {
    private Long productId;
    private String productName;
    private Long expectedQuantity;
    private Long actualQuantity;
}
