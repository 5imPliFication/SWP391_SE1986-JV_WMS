package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.PermissionDAO;
import com.example.dao.RoleDAO;
import com.example.dao.UserDAO;
import com.example.model.Permission;
import com.example.model.Role;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleService {

    private RoleDAO roleDAO;
    private UserDAO userDAO;
    private PermissionDAO PermissionDAO;

    public RoleService() {
        roleDAO = new RoleDAO();
        userDAO = new UserDAO();
        PermissionDAO = new PermissionDAO();
    }


    public List<Role> getAllRoles() {
        return roleDAO.findAll();
    }


    public void deleteRole(Long roleId) {
        roleDAO.deleteRole(roleId);
    }

    public Role getRoleById(Long id) {
        return roleDAO.findById(id);
    }

    public void createRole(String roleName, String description, boolean isActive, String[] permissionIds) {
        // business logic có thể thêm ở đây (validate, check trùng, ...)
        roleDAO.createNewRole(roleName, description, isActive, permissionIds);
    }

    public void updateRole(Role role) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            conn.setAutoCommit(false);

            roleDAO.update(conn, role);

            List<Permission> permissions = role.getPermissions();
            if (permissions == null) {
                permissions = new ArrayList<>();
            }

            List<Long> newPermissionIds = permissions.stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());

            PermissionDAO.updateRolePermissions(conn, role.getId(), newPermissionIds);

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Update role failed", e);
        }
    }

}
