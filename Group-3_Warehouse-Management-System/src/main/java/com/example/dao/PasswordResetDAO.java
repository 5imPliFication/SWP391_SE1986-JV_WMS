package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.PasswordReset;
import com.example.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PasswordResetDAO {

    public List<PasswordReset> getAll() {
        List<PasswordReset> passwordResetList = new ArrayList<>();
        String sql = """
                SELECT r.id, u.fullname, u.email, r.status, r.created_at
                FROM password_resets r
                JOIN users u ON r.user_id = u.id
                ORDER BY r.created_at DESC;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PasswordReset passwordReset = new PasswordReset();

                User user = new User();
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));

                passwordReset.setId(rs.getLong("id"));
                passwordReset.setUser(user);
                passwordReset.setStatus(rs.getString("status"));
                passwordReset.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                passwordResetList.add(passwordReset);
            }
            return passwordResetList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(long userId, String status) {
        String sql = """
                    INSERT INTO password_resets (user_id, status)
                    VALUES (?, ?)
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, status);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public boolean updateTokenAndStatus(long requestId, String token, LocalDateTime expiresAt, String status) {
//        String sql = """
//                    UPDATE password_reset_requests
//                    SET token = ?, expires_at = ?, status = ?
//                    WHERE id = ?
//                """;
//
//        try (Connection conn = DBConfig.getDataSource().getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, token);
//            ps.setTimestamp(2, Timestamp.valueOf(expiresAt));
//            ps.setString(3, status);
//            ps.setLong(4, requestId);
//
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public PasswordReset findByToken(String token) {
//        String sql = """
//                    SELECT *
//                    FROM password_reset_requests
//                    WHERE token = ?
//                """;
//
//        try (Connection conn = DBConfig.getDataSource().getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, token);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                PasswordReset passwordReset = new PasswordReset();
//
//                User user = new User();
//                user.setFullName(rs.getString("fullname"));
//                user.setEmail(rs.getString("email"));
//
//                passwordReset.setId(rs.getLong("id"));
//                passwordReset.setUser(user);
//                passwordReset.setToken(rs.getString("token"));
//                passwordReset.setStatus(rs.getString("status"));
//                passwordReset.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
//                passwordReset.setExpiresAt(LocalDateTime.parse(rs.getString("expires_at")));
//
//                return passwordReset;
//            }
//            return null;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public boolean updateStatusByToken(String token, String status) {
//        String sql = """
//                    UPDATE password_reset_requests
//                    SET status = ?
//                    WHERE token = ?
//                """;
//
//        try (Connection conn = DBConfig.getDataSource().getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, status);
//            ps.setString(2, token);
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public boolean updateStatusById(long passwordResetId, String status) {
        String sql = """
                    UPDATE password_resets
                    SET status = ?
                    WHERE id = ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setLong(2, passwordResetId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
