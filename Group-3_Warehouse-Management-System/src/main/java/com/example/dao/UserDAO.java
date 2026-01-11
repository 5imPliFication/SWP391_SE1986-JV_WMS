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

        String sql = """
        SELECT 
            u.id,
            u.fullname,
            u.email,
            u.is_active,
            r.id   AS role_id,
            r.name AS role_name
        FROM users u
        JOIN roles r ON u.role_id = r.id
    """;

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("is_active"));

                Role role = new Role();
                role.setId(rs.getInt("role_id"));
                role.setName(rs.getString("role_name"));

                user.setRole(role);

                listUsers.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listUsers;
    }

    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
        String sql = "UPDATE users SET fullname = ?, email = ?, role_id = ?, is_active = ? WHERE id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public User login(String email) {
        final String sql = """
        SELECT 
            u.id,
            u.fullname,
            u.email,
            u.password_hash,
            r.id   AS role_id,
            r.name AS role_name
        FROM users u
        LEFT JOIN roles r ON u.role_id = r.id
        WHERE u.email = ?
    """;

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("fullname"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));

                Role role = new Role();
                role.setId(rs.getInt("role_id"));
                role.setName(rs.getString("role_name"));

                u.setRole(role);
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();// replace with logger later
        }
        return null;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserById(int id) {

        String sql = """
        SELECT u.id, u.fullname, u.email, u.is_active,
               r.id AS role_id, r.name AS role_name
        FROM users u
        JOIN roles r ON u.role_id = r.id
        WHERE u.id = ?
    """;

        try (
                Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                Role role = new Role();

                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("is_active"));

                role.setId(rs.getInt("role_id"));
                role.setName(rs.getString("role_name"));
                user.setRole(role);

                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean insertUser(User user) {
        String sql = "insert into users(fullname, email, password_hash, role_id)\n"
                + "values (?, ?, ?, ?);";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT id, name FROM roles WHERE is_active = 1";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                list.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        // Giả sử bạn test với User ID = 1
        int testId = 1;
        User u = dao.findUserById(testId);

        if (u != null) {
            System.out.println("--- Test thành công ---");
            System.out.println("Họ tên: " + u.getFullName());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Role hiện tại: " + u.getRole().getId());
            System.out.println("Role hiện tại: " + u.getRole().getName());
            System.out.println("Trạng thái: " + (u.isActive() ? "Active" : "Inactive"));
        } else {
            System.out.println("--- Test thất bại: Không tìm thấy User với ID = " + testId + " ---");
        }
    }
}
