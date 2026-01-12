package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.User;
import com.example.model.RolePermission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HieuDAO {

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

    public void updateRolePermissions(String roleId, List<String> permissionIds) {
        String deleteSql = "DELETE FROM role_permissions WHERE role_id = ?";
        String insertSql = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";

        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                deletePs.setString(1, roleId);
                deletePs.executeUpdate();
            }

            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                for (String permissionId : permissionIds) {
                    insertPs.setString(1, roleId);
                    insertPs.setString(2, permissionId);
                    insertPs.addBatch();
                }
                insertPs.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Update role permissions failed");
        }
    }

    public static void main(String[] args) {
        try {
            HieuDAO dao = new HieuDAO();

            System.out.println("=== TEST viewRolePermission ===");

            List<RolePermission> list = dao.viewRolePermission("R02");

            if (list == null) {
                System.out.println("❌ LIST = NULL (DAO lỗi hoặc query sai)");
            } else if (list.isEmpty()) {
                System.out.println("⚠ LIST RỖNG (query chạy nhưng không có data)");
            } else {
                System.out.println("✅ SỐ LƯỢNG: " + list.size());
                for (RolePermission rp : list) {
                    System.out.println(
                            "RoleID=" + rp.getRoleID()
                            + " | PermissionID=" + rp.getPermissionID()
                            + " | PermissionName=" + rp.getPermissionName()
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("💥 EXCEPTION:");
            e.printStackTrace();
        }
    }

}
