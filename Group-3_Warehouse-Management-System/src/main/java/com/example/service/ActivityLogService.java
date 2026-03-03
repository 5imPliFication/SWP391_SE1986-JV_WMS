package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.UserActivityDAO;
import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.model.UserActivityLog;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ActivityLogService {
    private final UserDAO userDAO = new UserDAO();

    private final UserActivityDAO activityDAO = new UserActivityDAO();

    public void log(User user, String activity) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            activityDAO.log(conn, user, activity);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to log activity", e);
        }
    }


    public List<UserActivityLog> getRecentActivities(int limit) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {
            return activityDAO.findRecent(conn, limit);
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
        req.setAttribute("managerCount", userDAO.countUsersByRole("Manager"));
        req.setAttribute("salesmanCount", userDAO.countUsersByRole("Salesman"));
        req.setAttribute("storekeeperCount", userDAO.countUsersByRole("Warehouse"));

    }
}
