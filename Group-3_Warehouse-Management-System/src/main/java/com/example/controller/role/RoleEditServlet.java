package com.example.controller.role;

import com.example.model.Permission;
import com.example.model.Role;
import com.example.config.DBConfig;
import com.example.service.PermissionService;
import com.example.service.RoleService;
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

    private RoleService r;
    private PermissionService p;

    @Override
    public void init() {
        r = new RoleService();
        p = new PermissionService();
    }

    // ===================== GET =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Role role = r.getRoleById(id);
        List<Permission> allPermissions = p.getAllPermissions();

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

            r.updateRole( role);

            // 2️⃣ update role_permissions
            p.updateRolePermissions(
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
