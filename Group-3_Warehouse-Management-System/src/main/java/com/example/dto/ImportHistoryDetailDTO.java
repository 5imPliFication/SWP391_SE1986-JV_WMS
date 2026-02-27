package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportHistoryDetailDTO {
    private String productName;
    private Long expectedQuantity;
    private Long actualQuantity;
}
