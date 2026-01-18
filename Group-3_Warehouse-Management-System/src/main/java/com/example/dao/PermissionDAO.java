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
                = "        SELECT p.id, p.name "
                + "        FROM permissions p "
                + "        JOIN role_permissions rp ON p.id = rp.permission_id "
                + "        WHERE rp.role_id = ? ";
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

    public List<Permission> getAllPermissions() {
        List<Permission> listPermission = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM laptop_wms.permissions;");

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setId(rs.getLong("id"));
                    permission.setName(rs.getString("name"));
                    permission.setDescription(rs.getString("description"));
                    listPermission.add(permission);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPermission;
    }

    public List<RolePermission> viewRolePermission(String role_id) {
        List<RolePermission> listRolePermission = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT \n"
                + "    p.id   AS permission_id,\n"
                + "    p.name AS permission_name,\n"
                + "    r.role_id\n"
                + "FROM laptop_wms.permission p\n"
                + "JOIN laptop_wms.role_permissions r \n"
                + "    ON p.id = r.permission_id\n"
                + "WHERE r.role_id = ?");

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            ps.setString(1, role_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setPermissionID(rs.getLong("permission_id"));
                    rolePermission.setPermissionName(rs.getString("permission_name"));
                    rolePermission.setRoleID(rs.getLong("role_id"));
                    listRolePermission.add(rolePermission);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listRolePermission;
    }

    public void updateRolePermissions(Connection conn, long roleId, List<Long> permissionIds)
            throws SQLException {

        String deleteSql = "DELETE FROM role_permissions WHERE role_id = ?";
        String insertSql = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";

        // Xóa quyền cũ
        try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
            deletePs.setLong(1, roleId);
            deletePs.executeUpdate();
        }

        // Thêm quyền mới
        try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
            for (Long pid : permissionIds) {
                insertPs.setLong(1, roleId);
                insertPs.setLong(2, pid);
                insertPs.addBatch();
            }
            insertPs.executeBatch();
        }
    }

    //thêm filter permission ở đây
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
}
