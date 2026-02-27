package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {
    private Long id;
    private String name;
    private String description;
    private int isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
