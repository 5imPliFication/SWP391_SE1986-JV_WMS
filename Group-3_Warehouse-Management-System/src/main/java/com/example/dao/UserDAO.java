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
                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getObject("name", Role.class));
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


        String sql = "select u.fullname, u.email, u.is_active, r.name " +
                "from user as u join roles as r on u.role_id = r.id where u.id like ?";

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
                u.setRole(rs.getObject("name", Role.class));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace(); // replace with logger later
        }
        return null;
    }

    public boolean insertUser(User user) {
        String sql = "insert into user(fullname, email, password_hash, role_id)\n" +
                "values (?, ?, ?, ?);";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setLong(4, user.getRole().getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE user SET password_hash = ? WHERE email = ?";

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
        String sql = "SELECT password_hash FROM user WHERE email = ?";
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
            ps.setLong(3, user.getRole().getId());
            ps.setBoolean(4, user.isActive());
            ps.setLong(5, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean changeStatus(int id, boolean status) {
        String sql = "update user set is_active = ? where id = ?;";

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
        String sql = "select is_active from user where id = ?";

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
