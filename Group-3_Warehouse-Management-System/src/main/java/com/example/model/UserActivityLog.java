package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityLog {
    private Long id;
    private User user;
    private Timestamp lastLoginAt;
    private Timestamp lastLogoutAt;
    private Timestamp lastActivityAt;
    private Timestamp lastPasswordChangeAt;
    private Timestamp lastProfileUpdateAt;
    private String ipAddress;
    private String userAgent;
}
