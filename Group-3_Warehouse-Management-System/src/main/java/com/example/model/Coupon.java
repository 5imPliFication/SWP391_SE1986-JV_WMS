package com.example.model;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    private Long id;
    private String code;
    private String description;
    private String discountType;
    private Double discountValue;
    private Double minOrderAmount;
    private Date validFrom;
    private Date validUntil;
    private Integer usageLimit;
    private Integer usedCount;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
}