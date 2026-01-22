package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User findByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT \n"
                + "            u.id,\n"
                + "            u.fullname,\n"
                + "            u.email,\n"
                + "            u.password_hash,\n"
                + "            u.is_active,\n"
                + "            r.id   AS role_id,\n"
                + "            r.is_active as role_active,\n"
                + "            r.name AS role_name\n"
                + "        FROM users u\n"
                + "        LEFT JOIN roles r ON u.role_id = r.id\n"
                + "        WHERE u.email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            User user = new User();
            user.setId(rs.getLong("id"));
            user.setFullName(rs.getString("fullname"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setActive(rs.getBoolean("is_active"));

            Role role = new Role();
            role.setId(rs.getLong("role_id"));
            role.setName(rs.getString("role_name"));
            role.setActive(rs.getBoolean("role_active"));
            user.setRole(role);
            System.out.println("ROLE ID = " + user.getRole().getId());
            System.out.println("ROLE NAME = " + user.getRole().getName());
            System.out.println("ROLE ACTIVE = " + user.getRole().isActive());

            return user;
        }
    }

    public User findUserById(long id) {

        String sql = """
                    SELECT u.id, u.fullname, u.email, u.is_active,
                           r.id AS role_id, r.name AS role_name
                    FROM users u
                    JOIN roles r ON u.role_id = r.id
                    WHERE u.id = ?
                """;


        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            // get data
            if (rs.next()) {
                User userDetail = new User();
                Role role = new Role();

                userDetail.setId(rs.getLong("id"));
                userDetail.setFullName(rs.getString("fullname"));
                userDetail.setEmail(rs.getString("email"));
                userDetail.setActive(rs.getBoolean("is_active"));

                role.setId(rs.getLong("role_id"));
                role.setName(rs.getString("role_name"));
                userDetail.setRole(role);

                return userDetail;
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

    public boolean updateUserInformation(User user) {
        StringBuilder sql = new StringBuilder("update users "
                + "set fullname=?,email=?, role_id=?, is_active=? where id=?");
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setLong(3, user.getRole().getId());
            ps.setBoolean(4, user.isActive());
            ps.setLong(5, user.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get hashed password from database
    public String getPassword(Connection conn, String email) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public boolean updatePassword(Connection conn, String email, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            int rows = ps.executeUpdate();
            System.out.println("Password update rows = " + rows);
            return rows > 0;
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


    public Role findRoleByID(int roleId) {
        // Sửa SQL để lấy cả ID và Name của Permission, nối bằng dấu hai chấm
        String sql = "SELECT r.id, r.name, r.description, r.is_active, "
                + "GROUP_CONCAT(CONCAT(p.id, ':', p.name) SEPARATOR '|') AS p_info "
                + "FROM roles r "
                + "LEFT JOIN role_permissions rp ON r.id = rp.role_id "
                + "LEFT JOIN permissions p ON rp.permission_id = p.id "
                + "WHERE r.id = ? "
                + "GROUP BY r.id";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getLong("id"));
                    role.setName(rs.getString("name"));
                    role.setDescription(rs.getString("description"));
                    role.setActive(rs.getBoolean("is_active"));

                    List<Permission> permList = new ArrayList<>();
                    String pInfo = rs.getString("p_info");

                    if (pInfo != null && !pInfo.isEmpty()) {
                        String[] entries = pInfo.split("\\|");
                        for (String entry : entries) {
                            String[] parts = entry.split(":"); // Tách ID và Name
                            if (parts.length == 2) {
                                Permission p = new Permission();
                                p.setId(Long.parseLong(parts[0])); // QUAN TRỌNG: Phải có ID
                                p.setName(parts[1]);
                                permList.add(p);
                            }
                        }
                    }
                    role.setPermissions(permList);
                    return role;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAll(String searchName, String typeSort) {
        StringBuilder sql = new StringBuilder("select u.id, u.fullname, u.email, r.id as role_id, r.name as role_name, u.is_active from users u\n" +
                "join roles r on u.role_id = r.id where 1 = 1 ");
        List<User> listUsers = new ArrayList<>();

        // if param has value of searchName
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" and u.fullname like ? ");
        }

        // handle type sort
        if (typeSort != null && !typeSort.trim().isEmpty()) {
            if (typeSort.equalsIgnoreCase("asc")) {
                sql.append(" order by u.fullname asc");
            } else if (typeSort.equalsIgnoreCase("desc")) {
                sql.append(" order by u.fullname desc");
            }
        }

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // if searchName has value -> set value to query
            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(1, "%" + searchName + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("is_active"));
                Role role = new Role();
                role.setId(rs.getLong("role_id"));
                role.setName(rs.getString("role_name"));
                user.setRole(role);
                listUsers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listUsers;
    }
}
