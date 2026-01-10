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

    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);
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
