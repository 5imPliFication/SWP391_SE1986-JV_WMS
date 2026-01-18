/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.role;

import com.example.dao.PermissionDAO;
import com.example.dao.RoleDAO;
import com.example.dao.UserDAO;
import com.example.model.Permission;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author PC
 */
@WebServlet(name = "RoleCreateServlet", urlPatterns = {"/create-role"})
public class RoleCreateServlet extends HttpServlet {

    private UserDAO d;
    private RoleDAO r;
    private  PermissionDAO p;

    @Override
    public void init() {
        d = new UserDAO();
        r = new RoleDAO();
        p = new PermissionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Permission> allPermissions = p.getAllPermissions();
        request.setAttribute("listRolePermission", allPermissions);

        request.getRequestDispatcher("/WEB-INF/role/create-role.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String roleName = request.getParameter("roleName");
            String description = request.getParameter("description");
            boolean isActive = request.getParameter("status") != null && request.getParameter("status").equals("true");
            String[] permissionIds = request.getParameterValues("permissionIds");

            r.createNewRole(roleName, description, isActive, permissionIds);

            response.sendRedirect("roles?status=add_success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("roles?status=add_error");
        }
    }

}
