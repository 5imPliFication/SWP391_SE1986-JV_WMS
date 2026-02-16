package com.example.model;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    private Long id;
    private String code;
    private String description;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private Timestamp validFrom;
    private Timestamp validUntil;
    private Integer usageLimit;
    private Integer usedCount;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public boolean isValid() {
        if (!isActive) return false;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (validFrom != null && now.before(validFrom)) return false;
        if (validUntil != null && now.after(validUntil)) return false;

        return usageLimit == null || usedCount < usageLimit;
    }

}