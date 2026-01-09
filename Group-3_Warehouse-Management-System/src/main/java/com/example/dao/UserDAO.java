package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

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
                u.setId(rs.getString("id"));
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


}
