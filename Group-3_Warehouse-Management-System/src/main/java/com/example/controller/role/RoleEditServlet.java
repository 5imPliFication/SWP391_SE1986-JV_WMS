/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.role;

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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RoleEditServlet", urlPatterns = {"/edit-role"})
public class RoleEditServlet extends HttpServlet {

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

        Long id = Long.parseLong(request.getParameter("id"));

        Role role = r.findById(id);

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
            Long roleId = Long.parseLong(request.getParameter("roleID"));
            String roleName = request.getParameter("roleName");
            String description = request.getParameter("description");

            String[] permissionIds = request.getParameterValues("permissionIds");
            //convert from array of strings to List
            List<Permission> permissions = new ArrayList<>();
            if (permissionIds != null) {
                for (String pid : permissionIds) {
                    Permission p = new Permission();
                    p.setId(Long.parseLong(pid));
                    permissions.add(p);
                }
            }
            Role newRole = new Role(
                    roleId,
                    roleName,
                    description,
                    true,                          // isActive
                    new Timestamp(System.currentTimeMillis()), // createdAt
                    permissions
            );
            r.update(newRole);
            response.sendRedirect("roles?message=update_success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("roles?error=update_failed");
        }
    }
}
