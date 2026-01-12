package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Permission;
import com.example.model.Role;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleDAO {

    private final Connection conn;

    public RoleDAO(Connection conn) {
        this.conn = conn;
    }

    // 12. View role list
    public List<Role> findAll() throws SQLException {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM roles";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Role(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at"),
                    rs.getObject("permission", List.class)
            ));
        }
        return list;
    }

    // 13. View role detail
    public Role findById(String id) throws SQLException {
        String sql = "SELECT * FROM roles WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Role(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at"),
                    rs.getObject("permission", List.class)
            );
        }
        return null;
    }

    // 14. Update role info
    public void update(Role role) throws SQLException {
        String sql = "UPDATE roles SET name=?, description=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, role.getName());
        ps.setString(2, role.getDescription());
        ps.setLong(3, role.getId());
        ps.executeUpdate();
    }

    // 15. Active / Deactivate
    public void changeStatus(String id, boolean active) throws SQLException {
        String sql = "UPDATE roles SET is_active=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setBoolean(1, active);
        ps.setString(2, id);
        ps.executeUpdate();
    }

    public void createNewRole(String name, String description, boolean isActive, String[] permissionIds) {
        String sqlInsertRole = "INSERT INTO roles (name, description, is_active) VALUES (?, ?, ?)";
        String sqlInsertPerms = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false);

            int newRoleId = 0;
            // 1. Chèn Role mới và lấy ID tự động tạo
            try (PreparedStatement ps = conn.prepareStatement(sqlInsertRole, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setBoolean(3, isActive);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        newRoleId = rs.getInt(1);
                    }
                }
            }

            if (newRoleId > 0 && permissionIds != null && permissionIds.length > 0) {
                try (PreparedStatement ps = conn.prepareStatement(sqlInsertPerms)) {
                    for (String pId : permissionIds) {
                        ps.setInt(1, newRoleId);
                        ps.setInt(2, Integer.parseInt(pId));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteRole(int roleId) {
        String sqlDeleteMapping = "DELETE FROM role_permissions WHERE role_id = ?";
        String sqlDeleteRole = "DELETE FROM roles WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteMapping)) {
                ps1.setInt(1, roleId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteRole)) {
                ps2.setInt(1, roleId);
                ps2.executeUpdate();
            }

            conn.commit(); // Hoàn tất
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
