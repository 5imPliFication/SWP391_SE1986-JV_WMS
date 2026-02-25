package com.example.filter;

import java.util.HashMap;
import java.util.Map;

public class PermissionChecker {

    private static final Map<String, String> urlPermissionMap = new HashMap<>();

    static {
        urlPermissionMap.put("/user-list", "READ_USER");
        urlPermissionMap.put("/user-create", "CREATE_USER");
        urlPermissionMap.put("/user", "UPDATE_USER");
        urlPermissionMap.put("/user-delete", "UPDATE_ROLE");

        // --- QUẢN LÝ ROLE ---
        urlPermissionMap.put("/roles", "READ_ROLE");
        urlPermissionMap.put("/create-role", "CREATE_ROLE");
        urlPermissionMap.put("/edit-role", "UPDATE_ROLE");
        urlPermissionMap.put("/role-delete", "UPDATE_ROLE");

        // --- QUẢN LÝ PASSWORD RESET REQUEST ---
        urlPermissionMap.put("/admin/password-reset", "READ_PASSWORD_RESET_REQUEST");
    }

    public static String getRequiredPermission(String url) {
        return urlPermissionMap.get(url);
    }
}
