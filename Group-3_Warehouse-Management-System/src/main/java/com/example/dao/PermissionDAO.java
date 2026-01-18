package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Permission;
import com.example.model.RolePermission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionDAO {

    public List<Permission> findByRoleId(Connection conn, long roleId) throws SQLException {
        String sql
                = "SELECT p.id, p.name "
                + "FROM permissions p "
                + "JOIN role_permissions rp ON p.id = rp.permission_id "
                + "WHERE rp.role_id = ?";

        List<Permission> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                list.add(p);
            }
        }
        return list;
    }

    public List<Permission> getAllPermissions(Connection conn) throws SQLException {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT id, name, description FROM permissions";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                list.add(p);
            }
        }
        return list;
    }

    public void updateRolePermissions(Connection conn, long roleId, List<Long> permissionIds)
            throws SQLException {

        try (PreparedStatement ps
                = conn.prepareStatement("DELETE FROM role_permissions WHERE role_id=?")) {
            ps.setLong(1, roleId);
            ps.executeUpdate();
        }

        try (PreparedStatement ps
                = conn.prepareStatement(
                        "INSERT INTO role_permissions(role_id, permission_id) VALUES (?,?)")) {

            for (Long pid : permissionIds) {
                ps.setLong(1, roleId);
                ps.setLong(2, pid);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
