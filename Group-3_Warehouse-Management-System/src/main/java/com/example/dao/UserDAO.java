package com.example.dao;

import com.example.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
