package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonthSummaryDTO {
    private int month;
    private long importQuantity;
    private long exportQuantity;
}
