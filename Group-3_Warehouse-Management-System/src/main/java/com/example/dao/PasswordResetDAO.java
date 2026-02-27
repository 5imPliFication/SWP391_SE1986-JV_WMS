package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.PasswordReset;
import com.example.model.User;
import com.example.util.AppConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordResetDAO {

    public List<PasswordReset> getAll(String searchName, String requestStatus, int pageNo) {
        List<PasswordReset> passwordResetList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                    SELECT r.id, u.fullname, u.email, r.status, r.created_at
                    FROM password_resets r
                    JOIN users u ON r.user_id = u.id
                    WHERE 1=1
                """);

        // search by name
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND u.fullname LIKE ? ");
        }
        // filter by status
        if (requestStatus != null && !requestStatus.trim().isEmpty()) {
            sql.append(" AND r.status = ? ");
        }
        sql.append(" ORDER BY r.created_at DESC ");

        // handle pagination
        sql.append(" limit ? offset ? ");

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (requestStatus != null && !requestStatus.trim().isEmpty()) {
                ps.setString(index++, requestStatus.toUpperCase());
            }
            // set value for pagination of SQL
            ps.setInt(index++, AppConstants.PAGE_SIZE);

            int offset = (pageNo - 1)* AppConstants.PAGE_SIZE;
            ps.setInt(index++, offset);

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
                    INSERT INTO password_resets (user_id, status, created_at, updated_at)
                    VALUES (?, ?, NOW(), NOW())
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

    public int countRequests(String searchName, String requestStatus) {
        int totalOfRequests = 0;
        StringBuilder sql = new StringBuilder("""
                    SELECT count(*)
                    FROM password_resets r
                    JOIN users u ON r.user_id = u.id
                    WHERE 1=1
                """);
        // search by name
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND u.fullname LIKE ? ");
        }
        // filter by status
        if (requestStatus != null && !requestStatus.trim().isEmpty()) {
            sql.append(" AND r.status = ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (requestStatus != null && !requestStatus.trim().isEmpty()) {
                ps.setString(index++, requestStatus.toUpperCase());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                totalOfRequests = rs.getInt(1);
            }
            return totalOfRequests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
