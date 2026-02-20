/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.role;

import com.example.model.Permission;
import com.example.model.Role;
import com.example.service.PermissionService;
import com.example.service.RoleService;
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
@WebServlet(name = "RoleListServlet", urlPatterns = {"/roles"})
public class RoleListServlet extends HttpServlet {

    private RoleService r;
    private PermissionService p;

    @Override
    public void init() {
        r = new RoleService();
        p = new PermissionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = r.getAllRoles();
        List<Permission> allPermissions = p.getAllPermissions();

        if (roleList != null) {
            for (Role role : roleList) {
                List<Long> pIds = new java.util.ArrayList<>();
                if (role.getPermissions() != null) {
                    for (Permission perm : role.getPermissions()) {
                        pIds.add(perm.getId());
                    }
                }
                request.setAttribute("rolePermissionIds_" + role.getId(), pIds);
            }
        }

        request.setAttribute("roleList", roleList);
        request.setAttribute("allPermissions", allPermissions);
        request.getRequestDispatcher("/WEB-INF/role/roles.jsp").forward(request, response);
    }

}
