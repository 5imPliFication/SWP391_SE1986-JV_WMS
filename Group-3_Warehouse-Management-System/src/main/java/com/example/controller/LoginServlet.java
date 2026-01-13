package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.Permission;
import com.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static com.example.util.PasswordUtil.checkPassword;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = (req.getParameter("email")).trim();
        String password = req.getParameter("password").trim();

        User user = userDAO.existedByEmail(email);

        // Debug: Kiểm tra User lấy ra từ DB
        System.out.println("--- DEBUG LOGIN SERVLET ---");
        if (user != null) {
            System.out.println("Email login: " + user.getEmail());
            System.out.println("Role name: " + (user.getRole() != null ? user.getRole().getName() : "NULL ROLE"));
            System.out.println(password);
            System.out.println("Hashed password:" + user.getPasswordHash());
        } else {
            System.out.println("User object is NULL - Check email in Database");
        }

        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // FIX: Check user exists first, then verify password properly
        if (user == null || !checkPassword(password, user.getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        // --- XỬ LÝ VÀ DEBUG PERMISSIONS ---
        List<String> userPermissions = new ArrayList<>();

        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            userPermissions = user.getRole().getPermissions().stream()
                    .map(Permission::getName)
                    .collect(Collectors.toList());

            System.out.println("Permissions found: " + userPermissions);
        } else {
            System.out.println("WARNING: Permissions list is EMPTY or NULL for this user!");
        }

        session.setAttribute("userPermissions", userPermissions);
        System.out.println("Session 'userPermissions' has been set.");
        System.out.println("---------------------------");

        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
