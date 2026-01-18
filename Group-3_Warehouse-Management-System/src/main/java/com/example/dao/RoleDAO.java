package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Permission;
import com.example.model.Role;
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

    public List<Role> findAll() {

        List<Role> list = new ArrayList<>();
        String sql = "SELECT r.id, r.name, r.description, r.is_active, "
                + "GROUP_CONCAT(p.name SEPARATOR '|') AS p_names "
                + "FROM roles r "
                + "LEFT JOIN role_permissions rp ON r.id = rp.role_id "
                + "LEFT JOIN permissions p ON rp.permission_id = p.id "
                + "GROUP BY r.id";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                role.setDescription(rs.getString("description"));
                role.setActive(rs.getBoolean("is_active"));

                List<Permission> permList = new ArrayList<>();
                String pNames = rs.getString("p_names");

                if (pNames != null) {
                    String[] names = pNames.split("\\|");
                    for (String name : names) {
                        Permission p = new Permission();
                        p.setName(name);
                        permList.add(p);
                    }
                }
                role.setPermissions(permList);
                list.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Role role) throws SQLException {
        String sql = "UPDATE roles SET name=?, description=? WHERE id=?";
        Connection conn = DBConfig.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, role.getName());
        ps.setString(2, role.getDescription());
        ps.setLong(3, role.getId());
        ps.executeUpdate();
    }

    public boolean changeStatus(Long id, boolean status) {
        String sql = "update roles set is_active = ? where id = ?;";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, status);
            ps.setLong(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Role findById(Long roleId) {
        // Sửa SQL để lấy cả ID và Name của Permission, nối bằng dấu hai chấm
        String sql = "SELECT r.id, r.name, r.description, r.is_active, "
                + "GROUP_CONCAT(CONCAT(p.id, ':', p.name) SEPARATOR '|') AS p_info "
                + "FROM roles r "
                + "LEFT JOIN role_permissions rp ON r.id = rp.role_id "
                + "LEFT JOIN permissions p ON rp.permission_id = p.id "
                + "WHERE r.id = ? "
                + "GROUP BY r.id";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getLong("id"));
                    role.setName(rs.getString("name"));
                    role.setDescription(rs.getString("description"));
                    role.setActive(rs.getBoolean("is_active"));

                    List<Permission> permList = new ArrayList<>();
                    String pInfo = rs.getString("p_info");

                    if (pInfo != null && !pInfo.isEmpty()) {
                        String[] entries = pInfo.split("\\|");
                        for (String entry : entries) {
                            String[] parts = entry.split(":"); // Tách ID và Name
                            if (parts.length == 2) {
                                Permission p = new Permission();
                                p.setId(Long.parseLong(parts[0])); // QUAN TRỌNG: Phải có ID
                                p.setName(parts[1]);
                                permList.add(p);
                            }
                        }
                    }
                    role.setPermissions(permList);
                    return role;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public void deleteRole(Long roleId) {
        String sqlDeleteMapping = "DELETE FROM role_permissions WHERE role_id = ?";
        String sqlDeleteRole = "DELETE FROM roles WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConfig.getDataSource().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteMapping)) {
                ps1.setLong(1, roleId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteRole)) {
                ps2.setLong(1, roleId);
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