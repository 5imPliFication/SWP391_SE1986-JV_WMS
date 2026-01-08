package com.example.model;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    private String id;
    private String fullName;
    private String email;
    private String passwordHash;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;
}
