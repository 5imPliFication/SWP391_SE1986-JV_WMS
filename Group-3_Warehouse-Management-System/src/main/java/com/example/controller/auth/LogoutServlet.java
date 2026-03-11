package com.example.controller.auth;

import com.example.config.DBConfig;
import com.example.dao.UserActivityDAO;
import com.example.model.User;
import com.example.service.ActivityLogService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private final ActivityLogService activityLogService = new ActivityLogService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                activityLogService.log(user, "Login");
            }
            session.invalidate();
        }

        resp.sendRedirect(req.getContextPath()+ "/login");
    }
}
