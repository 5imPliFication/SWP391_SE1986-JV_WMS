package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoodsReceiptItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long expectedQuantity;
    private Long actualQuantity;
    private List<ProductItemDTO> productItems;
}
