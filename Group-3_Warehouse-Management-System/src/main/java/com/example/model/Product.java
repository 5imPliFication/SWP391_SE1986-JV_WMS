package com.example.model;

import lombok.*;

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
    private Boolean isActive;
    private Brand brand;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
