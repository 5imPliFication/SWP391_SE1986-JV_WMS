package com.example.controller;

import com.example.model.User;
import com.example.model.UserActivityLog;
import com.example.service.ActivityLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/home")
public class HomeServlet extends HttpServlet {

    private final ActivityLogService dashboardService = new ActivityLogService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole().getName();

        if ("Admin".equals(role)) {
            List<UserActivityLog> recentActivities =
                    dashboardService.getRecentActivities(10);
            dashboardService.loadAdminDashboard(req);
            req.setAttribute("recentActivities", recentActivities);
        } else if ("Staff".equals(user.getRole().getName())) {
            dashboardService.loadStaffDashboard(req);
        }

        req.getRequestDispatcher("/WEB-INF/home.jsp")
                .forward(req, resp);
    }
}
