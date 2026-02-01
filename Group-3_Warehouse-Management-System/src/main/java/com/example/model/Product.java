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
}
