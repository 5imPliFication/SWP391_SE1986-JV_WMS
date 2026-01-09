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
    public List<User> findAll(){
        List<User> listUsers = new ArrayList<>();

        // sql
        StringBuilder sql = new StringBuilder("select u.id, fullname, email, r.name, u.is_active " +
                "from user as u join roles as r on u.role_id = r.id" );

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            // get data
            while (rs.next()){
                User user = new User();
                user.setId(rs.getString("id"));
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
}
