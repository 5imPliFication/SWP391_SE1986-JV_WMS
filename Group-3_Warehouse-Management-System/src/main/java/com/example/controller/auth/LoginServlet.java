package com.example.controller.auth;

import com.example.config.DBConfig;
import com.example.dao.UserActivityDAO;
import com.example.dao.UserDAO;
import com.example.model.Permission;
import com.example.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static com.example.util.PasswordUtil.checkPassword;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userService.getUserByEmail(email);

        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // Check user exists first, then verify password properly
        if (user == null || !checkPassword(password, user.getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/WEB-INF/auth/login.jsp").forward(req, resp);
            return;
        }

        if (!user.isActive()) {
            req.setAttribute("error", "Account is inactive");
            req.getRequestDispatcher("/WEB-INF/auth/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            new UserActivityDAO().log(conn, user, "Login");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // --- XỬ LÝ VÀ DEBUG PERMISSIONS ---
        List<String> userPermissions = new ArrayList<>();
        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            // Chuyển đổi List<Permission> thành List<String> (tên quyền)
            userPermissions = user.getRole().getPermissions().stream()
                    .map(Permission::getName)
                    .collect(Collectors.toList());
        } else {
            System.out.println("WARNING: Permissions list is EMPTY or NULL for this user!");
        }

        // Lưu danh sách String tên quyền vào Session cho RoleFilter
        session.setAttribute("userPermissions", userPermissions);

        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
