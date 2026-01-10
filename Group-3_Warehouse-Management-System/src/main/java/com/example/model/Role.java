package com.example.model;

import lombok.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
    private Timestamp createdAt;


}
