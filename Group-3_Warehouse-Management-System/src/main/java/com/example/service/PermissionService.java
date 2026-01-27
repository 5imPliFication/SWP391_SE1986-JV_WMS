package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.PermissionDAO;
import com.example.model.Permission;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PermissionService {

    private final PermissionDAO permissionDAO = new PermissionDAO();

    public List<Permission> getAllPermissions() {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            return permissionDAO.getAllPermissions(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Permission> getPermissionsByRole(long roleId) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            return permissionDAO.findByRoleId(conn, roleId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRolePermissions(long roleId, List<Long> permissionIds) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            permissionDAO.updateRolePermissions(conn, roleId, permissionIds);

            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException("Update role permissions failed", e);
        }
    }
}
