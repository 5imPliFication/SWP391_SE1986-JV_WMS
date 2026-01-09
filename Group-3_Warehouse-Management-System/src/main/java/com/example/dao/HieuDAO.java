package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HieuDAO {

    public void updateUserInformation(User user) {
        StringBuilder sql = new StringBuilder("update users "
                + "set fullname=?,email=?, role_id=?, is_active=?"
                + "where id=?");

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString()); ResultSet rs = ps.executeQuery()) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole());
            ps.setBoolean(4, user.isActive());
            ps.setString(5, user.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
