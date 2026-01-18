package com.example.filter;

import java.util.HashMap;
import java.util.Map;

public class PermissionChecker {

    private static final Map<String, String> urlPermissionMap = new HashMap<>();

    static {
        urlPermissionMap.put("/user-list", "READ_USER");
        urlPermissionMap.put("/user-create", "CREATE_USER");
        urlPermissionMap.put("/user", "UPDATE_USER");
        urlPermissionMap.put("/user-delete", "DELETE_USER");

        // --- QUẢN LÝ ROLE ---
        urlPermissionMap.put("/roles", "READ_ROLE");
        urlPermissionMap.put("/create-role", "CREATE_ROLE");
        urlPermissionMap.put("/edit-role", "UPDATE_ROLE");
        urlPermissionMap.put("/role-delete", "DELETE_ROLE");
    }

    public static String getRequiredPermission(String url) {
        return urlPermissionMap.get(url);
    }
}
