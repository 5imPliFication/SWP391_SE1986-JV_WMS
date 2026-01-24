package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.UserActivityDAO;
import com.example.dao.UserDAO;
import com.example.model.UserActivityLog;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DashboardService {
    private final UserDAO userDAO = new UserDAO();

    private final UserActivityDAO activityDAO = new UserActivityDAO();

    public List<UserActivityLog> getRecentActivities(int limit) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            return activityDAO.findRecentActivities(conn, limit);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load recent activities", e);
        }
    }

    public void loadAdminDashboard(HttpServletRequest req) {

        req.setAttribute("totalUsers", userDAO.countAllUsers());
        req.setAttribute("activeUsers", userDAO.countActiveUsers());
        req.setAttribute("inactiveUsers", userDAO.countInactiveUsers());

        req.setAttribute("adminCount", userDAO.countUsersByRole("Admin"));
        req.setAttribute("staffCount", userDAO.countUsersByRole("Staff"));
        req.setAttribute("managerCount", userDAO.countUsersByRole("MANAGER"));
        req.setAttribute("salesmanCount", userDAO.countUsersByRole("SALESMAN"));
        req.setAttribute("storekeeperCount", userDAO.countUsersByRole("STOREKEEPER"));

    }

    public void loadStaffDashboard(HttpServletRequest req) {

        req.setAttribute("totalUsers", userDAO.countAllUsers());
        req.setAttribute("activeUsers", userDAO.countActiveUsers());
        req.setAttribute("inactiveUsers", userDAO.countInactiveUsers());

        req.setAttribute("adminCount", userDAO.countUsersByRole("Admin"));
        req.setAttribute("staffCount", userDAO.countUsersByRole("Staff"));
        req.setAttribute("managerCount", userDAO.countUsersByRole("MANAGER"));
        req.setAttribute("salesmanCount", userDAO.countUsersByRole("SALESMAN"));
        req.setAttribute("storekeeperCount", userDAO.countUsersByRole("STOREKEEPER"));

    }
}
