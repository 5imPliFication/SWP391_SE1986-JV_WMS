package com.example.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {
    private Long id;

    private Long productId;

    private Long quantity;

    private MovementType type;

    private ReferenceType referenceType;

    private LocalDateTime createdAt;
}
