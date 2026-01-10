package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Role;
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
        String sql = "select u.id, full_name, email, r.name, u.is_active " +
                "from users as u join roles as r on u.role_id = r.id";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // get data
            while (rs.next()){
                User user = new User();
                Role role = new Role();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                role.setName(rs.getString("name"));
                user.setRole(role);
                user.setActive(rs.getBoolean("is_active"));
                listUsers.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // return list users
        return listUsers;
    }

    public User findUserById(int id) {


        String sql = "select full_name, email, u.is_active, r.name " +
                "from users as u join roles as r on u.role_id = r.id where u.id like ?";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + id + "%");
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()){
                User userDetail = new User();
                Role role = new Role();
                userDetail.setFullName(rs.getString("full_name"));
                userDetail.setEmail(rs.getString("email"));
                role.setName(rs.getString("name"));
                userDetail.setRole(role);
                userDetail.setActive(rs.getBoolean("is_active"));
                return userDetail;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public boolean insertUser(User user) {
        String sql = "insert into users(full_name, email, password_hash, role_id)\n" +
                "values (?, ?, ?, ?);";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setInt(4, user.getRole().getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isExistEmail(String email) {
        String sql = "select email from users where email = ?";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean changeStatus(int id, boolean status) {
        String sql = "update users set is_active = ? where id = ?;";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getCurrentStatus(int id) {
        String sql = "select is_active from users where id = ?";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_active");
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
