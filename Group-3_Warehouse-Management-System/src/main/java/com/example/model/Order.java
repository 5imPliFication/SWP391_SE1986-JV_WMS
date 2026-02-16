package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String orderCode;
    private String customerName;
    private String customerPhone;
    private String note;
    private String status;
    private User createdBy;
    private Timestamp createdAt;
    private User processedBy;
    private Timestamp processedAt;
    private BigDecimal total;
    private Coupon coupon;
    private BigDecimal discountAmount;
    private BigDecimal finalTotal;


}
