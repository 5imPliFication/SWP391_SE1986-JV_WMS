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

    public void log(Connection conn, User user, String activity)
            throws SQLException {

        String sql = """
                    INSERT INTO user_activity_log (user_id, activity)
                    VALUES (?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, user.getId());
            ps.setString(2, activity);
            ps.executeUpdate();
        }
    }


    public List<UserActivityLog> findRecent(Connection conn, int limit)
            throws SQLException {

        String sql = """
                    SELECT
                        l.id,
                        l.activity,
                        l.created_at,
                        u.id        AS user_id,
                        u.email,
                        u.is_active,
                        r.name      AS role_name
                    FROM user_activity_log l
                    JOIN users u ON l.user_id = u.id
                    JOIN roles r ON u.role_id = r.id
                    ORDER BY l.created_at DESC
                    LIMIT ?
                """;

        List<UserActivityLog> logs = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Role role = new Role();
                role.setName(rs.getString("role_name"));

                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("is_active"));
                user.setRole(role);

                UserActivityLog log = new UserActivityLog();
                log.setId(rs.getLong("id"));
                log.setUser(user);
                log.setActivity(rs.getString("activity"));
                log.setTime(
                        rs.getTimestamp("created_at")
                );

                logs.add(log);
            }
        }
        return logs;
    }

}


