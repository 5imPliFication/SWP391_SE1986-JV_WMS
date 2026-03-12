package com.example.model;
import com.example.enums.MovementType;
import com.example.enums.ReferenceType;
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
    private String productName;

    private Long quantity;

    private MovementType type;

    private ReferenceType referenceType;
    
    private Long referenceId;

    private LocalDateTime createdAt;
}
