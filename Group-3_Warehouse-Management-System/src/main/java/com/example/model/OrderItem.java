package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Order order;
    private ProductItem productItem;
    private Product product;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}