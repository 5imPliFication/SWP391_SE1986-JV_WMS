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

/**
 *
 * @author PC
 */
@WebServlet(name = "Role", urlPatterns = {"/roles"})
public class RoleList extends HttpServlet {

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
        List<Role> roleList = r.getAllRoles();
        List<Permission> allPermissions = d.getAllPermissions();
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
                int roleId = Integer.parseInt(request.getParameter("id"));

                r.deleteRole(roleId);

                response.sendRedirect("roles?msg=delete_success");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("roles?msg=error");
    }

}
