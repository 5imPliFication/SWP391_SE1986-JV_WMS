package com.example.controller;

import com.example.model.Permission;
import com.example.service.RoleService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "CreateRole", urlPatterns = {"/create-role"})
public class CreateRole extends HttpServlet {

    private RoleService roleService;

    @Override
    public void init() {
        roleService = new RoleService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Permission> allPermissions = roleService.getAllPermissions();
        request.setAttribute("listRolePermission", allPermissions);

        request.getRequestDispatcher("create-role.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String roleName = request.getParameter("roleName");
            String description = request.getParameter("description");
            boolean isActive = "true".equals(request.getParameter("status"));
            String[] permissionIds = request.getParameterValues("permissionIds");

            roleService.createRole(roleName, description, isActive, permissionIds);

            response.sendRedirect("roles?status=add_success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("roles?status=add_error");
        }
    }
}
