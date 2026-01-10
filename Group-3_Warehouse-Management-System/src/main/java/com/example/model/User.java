package com.example.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String email;
    private String passwordHash;
    private String fullName;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
}
