package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserActivityLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserActivityDAO {
    public void logLogin(Connection conn, long userId) throws SQLException {
        String sql = """
                    INSERT INTO user_activity_logs (user_id, last_login_at, last_activity_at)
                    VALUES (?, NOW(), NOW())
                    ON DUPLICATE KEY UPDATE
                        last_login_at = NOW(),
                        last_activity_at = NOW()
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    public void logLogout(Connection conn, long userId) throws SQLException {
        String sql = """
                    UPDATE user_activity_logs
                    SET last_logout_at = NOW(),
                        last_activity_at = NOW()
                    WHERE user_id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    public void logPasswordChange(Connection conn, long userId) throws SQLException {
        String sql = """
                    UPDATE user_activity_logs
                    SET last_password_change_at = NOW()
                    WHERE user_id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    public void initActivity(long userId) {
        String sql = "INSERT INTO user_activity_logs (user_id) VALUES (?)";
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserActivityLog> findRecentActivities(Connection conn, int limit)
            throws SQLException {

        String sql = """
                    SELECT 
                        u.id              AS user_id,
                        u.email,
                        u.is_active,
                        r.id AS role_id,
                        r.name AS role_name,
                        r.is_active AS role_active,
                        ua.last_login_at,
                        ua.last_activity_at
                    FROM user_activity_logs ua
                    JOIN users u ON ua.user_id = u.id
                    JOIN roles r ON u.role_id = r.id
                    ORDER BY ua.last_activity_at DESC
                    LIMIT ?
                """;

        List<UserActivityLog> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("is_active"));

                Role role = new Role();
                role.setId(rs.getLong("role_id"));
                role.setName(rs.getString("role_name"));
                role.setActive(rs.getBoolean("role_active"));
                user.setRole(role);

                UserActivityLog activity = new UserActivityLog();
                activity.setUser(user);
                activity.setLastLoginAt(rs.getTimestamp("last_login_at"));
                activity.setLastActivityAt(rs.getTimestamp("last_activity_at"));

                result.add(activity);
            }
        }

        return result;
    }
}

