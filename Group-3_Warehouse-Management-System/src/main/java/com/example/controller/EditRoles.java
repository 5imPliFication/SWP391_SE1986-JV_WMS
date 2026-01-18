package com.example.controller;

import com.example.model.Permission;
import com.example.model.Role;
import com.example.service.RoleService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "EditRoles", urlPatterns = {"/edit-role"})
public class EditRoles extends HttpServlet {

    private RoleService roleService;

    @Override
    public void init() {
        roleService = new RoleService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Role role = roleService.getRoleById(id);
        List<Permission> allPermissions = roleService.getAllPermissions();

        request.setAttribute("role", role);
        request.setAttribute("listRolePermission", allPermissions);

        request.getRequestDispatcher("/edit-role.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Long roleId = Long.parseLong(request.getParameter("roleID"));
            String roleName = request.getParameter("roleName");
            String description = request.getParameter("description");

            boolean isActive = true;
            String[] permissionIdsArr = request.getParameterValues("permissionIds");

            List<String> permissionIds = permissionIdsArr != null
                    ? Arrays.asList(permissionIdsArr)
                    : List.of();

            roleService.updateRole(roleId,roleName,description,isActive,permissionIds);

            response.sendRedirect("roles?message=update_success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("roles?error=update_failed");
        }
    }
}
