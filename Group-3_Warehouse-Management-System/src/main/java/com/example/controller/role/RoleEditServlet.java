package com.example.controller.role;

import com.example.dao.PermissionDAO;
import com.example.dao.RoleDAO;
import com.example.model.Permission;
import com.example.model.Role;
import com.example.config.DBConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RoleEditServlet", urlPatterns = {"/edit-role"})
public class RoleEditServlet extends HttpServlet {

    private RoleDAO roleDAO;
    private PermissionDAO permissionDAO;
    private PermissionDAO PermissionDAO;

    @Override
    public void init() {
        roleDAO = new RoleDAO();
        permissionDAO = new PermissionDAO();
        PermissionDAO = new PermissionDAO();
    }

    // ===================== GET =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Role role = roleDAO.findById(id);
        List<Permission> allPermissions = permissionDAO.getAllPermissions();

        request.setAttribute("role", role);
        request.setAttribute("listRolePermission", allPermissions);

        request.getRequestDispatcher("/WEB-INF/role/edit-role.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            conn.setAutoCommit(false); // 🔥 transaction

            Long roleId = Long.parseLong(request.getParameter("roleID"));
            String roleName = request.getParameter("roleName");
            String description = request.getParameter("description");

            String[] permissionIds = request.getParameterValues("permissionIds");

            // convert permissionIds -> List<Long>
            List<Long> permissionIdList = new ArrayList<>();
            if (permissionIds != null) {
                for (String pid : permissionIds) {
                    permissionIdList.add(Long.parseLong(pid));
                }
            }

            Role role = new Role(
                    roleId,
                    roleName,
                    description,
                    true,
                    new Timestamp(System.currentTimeMillis()),
                    null
            );

            roleDAO.update(conn, role);

            // 2️⃣ update role_permissions
            PermissionDAO.updateRolePermissions(
                    conn,
                    roleId,
                    permissionIdList
            );

            conn.commit(); // ✅ OK

            response.sendRedirect("roles?message=update_success");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("roles?error=update_failed");
        }
    }
}
