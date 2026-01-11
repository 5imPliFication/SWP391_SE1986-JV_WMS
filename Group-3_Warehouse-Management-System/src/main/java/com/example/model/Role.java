package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private List<Permission> permissions;
    private LocalDateTime createdAt;
}
