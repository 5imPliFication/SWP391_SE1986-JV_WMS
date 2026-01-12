/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller;

import com.example.dao.UserDAO;
import com.example.dao.RoleDAO;
import com.example.model.Permission;
import com.example.model.Role;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "EditRoles", urlPatterns = {"/edit-role"})
public class EditRoles extends HttpServlet {

    private UserDAO d;
    private RoleDAO r;

    @Override
    public void init() {
        d = new UserDAO();
        r = new RoleDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Role role = r.findRoleByID(id);

        List<Permission> allPermissions = d.getAllPermissions();

        request.setAttribute("role", role);
        request.setAttribute("listRolePermission", allPermissions);

        request.getRequestDispatcher("/edit-role.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int roleId = Integer.parseInt(request.getParameter("roleID"));
            String roleName = request.getParameter("roleName");
            String description = request.getParameter("description");

            String[] permissionIds = request.getParameterValues("permissionIds");
            r.updateRoleFull(roleId, roleName, description, permissionIds);
            response.sendRedirect("roles?message=update_success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("roles?error=update_failed");
        }
    }
}
