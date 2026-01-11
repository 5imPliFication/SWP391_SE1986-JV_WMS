package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String email;
    private String passwordHash;
    private String fullName;
    private Role role;
    private boolean active;
    private Timestamp createdAt;
}
