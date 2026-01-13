package com.example.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDateTime createdAt;

    public boolean hasPermission(String permissionName) {
        if (role == null || role.getPermissions() == null || !role.isActive()) {
            return false;
        }
        return role.getPermissions().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(permissionName));
    }
}
