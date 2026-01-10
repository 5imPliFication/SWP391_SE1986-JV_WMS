package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String email;
    private String passwordHash;
    private String fullName;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastEditedAt;
}
