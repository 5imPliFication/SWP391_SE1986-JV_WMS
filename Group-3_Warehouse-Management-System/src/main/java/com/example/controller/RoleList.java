package com.example.controller;

import com.example.model.Permission;
import com.example.model.Role;
import com.example.service.RoleService;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Role", urlPatterns = {"/roles"})
public class RoleList extends HttpServlet {

    private RoleService roleService;

    @Override
    public void init() {
        roleService = new RoleService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Role> roleList = roleService.getAllRoles();
        List<Permission> allPermissions = roleService.getAllPermissions();

        request.setAttribute("roleList", roleList);
        request.setAttribute("allPermissions", allPermissions);

        request.getRequestDispatcher("/roles.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            try {
                Long roleId = Long.parseLong(request.getParameter("id"));
                roleService.deleteRole(roleId);

                response.sendRedirect("roles?msg=delete_success");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("roles?msg=error");
    }
}
