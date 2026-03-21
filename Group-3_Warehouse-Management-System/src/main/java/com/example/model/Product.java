package com.example.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    private Long id;
    private String name;
    private String description;
    private String imgUrl;
    private Long totalQuantity;
    private Boolean isActive;
    private Brand brand;
    private Category category;
    private Unit unit;
    private Chip chip;
    private Model model;
    private Ram ram;
    private Size size;
    private Storage storage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Not exist in database, just for display
    private Double currentPrice;

    private List<ProductItem> productItems;

    public Product(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
