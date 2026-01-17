package com.example.dao;

import com.example.model.Permission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO {
    public List<Permission> findByRoleId(Connection conn, long roleId) throws SQLException {
        String sql =
                "        SELECT p.id, p.name " +
                        "        FROM permissions p " +
                        "        JOIN role_permissions rp ON p.id = rp.permission_id " +
                        "        WHERE rp.role_id = ? ";
        List<Permission> permissions = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                permissions.add(p);
            }
        }
        return permissions;
    }
}
