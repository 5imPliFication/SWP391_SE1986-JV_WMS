package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> findAll() {
        List<User> listUsers = new ArrayList<>();

        // sql
        StringBuilder sql = new StringBuilder("select u.id, fullname, email, r.name, u.is_active "
                + "from user as u join roles as r on u.role_id = r.id");

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString()); ResultSet rs = ps.executeQuery()) {

            // get data
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("name"));
                user.setActive(rs.getBoolean("is_active"));
                listUsers.add(user);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // return list users
        return listUsers;
    }

    public User login(String email) {
        final String sql = "SELECT id,fullname,email,password_hash,role_id FROM user WHERE email = ?";

        try (
                Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setFullName(rs.getString("fullname"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role_id"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace(); // replace with logger later
        }
        return null;
    }



    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);

            int rowsUpdated = ps.executeUpdate();

            return rowsUpdated > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get hashed password from database
    public String getPassword(String email) {
        String sql = "SELECT password_hash FROM users WHERE email = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password_hash");
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void updateUserInformation(User user) {
        StringBuilder sql = new StringBuilder("update users "
                + "set fullname=?,email=?, role_id=?, is_active=? where id=?");
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getRole().getId());
            ps.setBoolean(4, user.isActive());
            ps.setInt(5, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
