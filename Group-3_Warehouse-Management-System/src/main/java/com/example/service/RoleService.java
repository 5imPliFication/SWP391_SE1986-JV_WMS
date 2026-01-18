package com.example.service;

import com.example.dao.RoleDAO;
import com.example.dao.UserDAO;
import com.example.model.Permission;
import com.example.model.Role;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RoleService {

    private RoleDAO roleDAO;
    private UserDAO userDAO;

    public RoleService() {
        roleDAO = new RoleDAO();
        userDAO = new UserDAO();
    }

    public List<Role> getAllRoles() {
        return roleDAO.findAll();
    }

    public List<Permission> getAllPermissions() {
        return userDAO.getAllPermissions();
    }

    public void deleteRole(Long roleId) {
        roleDAO.deleteRole(roleId);
    }


    public Role getRoleById(Long id) {
        return roleDAO.findById(id);
    }

 
    public void createRole(
            String roleName,
            String description,
            boolean isActive,
            String[] permissionIds
    ) {
        // business logic có thể thêm ở đây (validate, check trùng, ...)
        roleDAO.createNewRole(roleName, description, isActive, permissionIds);
    }

    public void updateRole(
            Long roleId,
            String roleName,
            String description,
            boolean isActive,
            List<String> permissionIds
    ) {

        if (roleId == null) {
            throw new IllegalArgumentException("Role ID is required");
        }

        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name is required");
        }

        roleDAO.updateRole(roleId, roleName, description, isActive, permissionIds);
    }
}
