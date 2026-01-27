package com.example.controller.permission;

import com.example.model.Role;
import com.example.service.PermissionService;
import com.example.service.RoleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/update-permissions"})
public class PermissionListServlet extends HttpServlet {

    private PermissionService p;

    @Override
    public void init() {
        p = new PermissionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy danh sách roleId từ form
            String[] roleParams = request.getParameterValues("roleIds");

            if (roleParams == null) {
                throw new RuntimeException("No roleIds found in request");
            }

            for (String r : roleParams) {
                long roleId = Long.parseLong(r);

                String paramName = "perm_" + roleId;
                String[] permissionParams = request.getParameterValues(paramName);

                List<Long> permissionIds = new ArrayList<>();

                if (permissionParams != null) {
                    for (String pid : permissionParams) {
                        permissionIds.add(Long.parseLong(pid));
                    }
                }

                // Update từng role
                p.updateRolePermissions(roleId, permissionIds);
            }

            response.sendRedirect("roles?msg=update_success");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("ERROR: " + e.getMessage());
        }
    }

}
