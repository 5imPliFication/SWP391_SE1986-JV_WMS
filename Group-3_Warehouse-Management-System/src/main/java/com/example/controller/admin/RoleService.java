package com.example.controller.admin;

import com.example.config.DBConfig;
import com.example.dao.RoleDAO;
import com.example.model.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/admin/role")
public class RoleService extends HttpServlet {

    private RoleDAO roleDAO;
    private Connection conn;

    @Override
    public void init() {
        try {
            DataSource ds = DBConfig.getDataSource();

            conn = ds.getConnection();

            roleDAO = new RoleDAO(conn);

        } catch (Exception e) {
            throw new RuntimeException("Cannot init RoleServlet", e);
        }
    }

    // ================== GET ==================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            if (action == null) {
                // 12. View role list
                List<Role> roles = roleDAO.findAll();
                req.setAttribute("roles", roles);
                req.getRequestDispatcher("/admin/role/list.jsp").forward(req, resp);

            } else if ("detail".equals(action)) {
                // 13. View role detail
                String id = req.getParameter("id");
                Role role = roleDAO.findById(id);
                req.setAttribute("role", role);
                req.getRequestDispatcher("/admin/role/detail.jsp").forward(req, resp);

            } else if ("toggle".equals(action)) {
                // 15. Active / Deactivate role
                String id = req.getParameter("id");
                boolean status = Boolean.parseBoolean(req.getParameter("status"));
                roleDAO.changeStatus(id, status);
                resp.sendRedirect("role");
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ================== POST ==================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Role role = new Role(
                    Long.parseLong(req.getParameter("id")),
                    req.getParameter("name"),
                    req.getParameter("description"),
                    true,
                    null,
                    null
            );

            roleDAO.update(role);
            resp.sendRedirect("role");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ================== DESTROY ==================
    @Override
    public void destroy() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close(); // trả connection về pool
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
