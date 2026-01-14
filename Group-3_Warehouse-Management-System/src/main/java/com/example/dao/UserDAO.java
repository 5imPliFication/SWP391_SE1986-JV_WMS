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

import static com.example.util.PasswordUtil.checkPassword;
import static com.example.util.PasswordUtil.hashPassword;

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

        // return list users
        return listUsers;
    }


    public User existedByEmail(String email) {
        // 1. Câu lệnh SQL lấy User và Role
        final String sql = """
                    SELECT 
                        u.id,
                        u.fullname,
                        u.email,
                        u.password_hash,
                        u.is_active,
                        r.id   AS role_id,
                        r.name AS role_name
                    FROM users u
                    LEFT JOIN roles r ON u.role_id = r.id
                    WHERE u.email = ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setFullName(rs.getString("fullname"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setActive(rs.getBoolean("is_active"));

                // 2. Khởi tạo đối tượng Role
                Role role = new Role();
                long roleId = rs.getLong("role_id");
                role.setId(roleId);
                role.setName(rs.getString("role_name"));

                // --- BẮT ĐẦU LẤY DANH SÁCH PERMISSION ---
                List<Permission> permissions = new ArrayList<>();
                final String sqlPerms = """
                            SELECT p.id, p.name 
                            FROM permissions p
                            JOIN role_permissions rp ON p.id = rp.permission_id
                            WHERE rp.role_id = ?
                        """;

                try (PreparedStatement psP = conn.prepareStatement(sqlPerms)) {
                    psP.setLong(1, roleId);
                    ResultSet rsP = psP.executeQuery();
                    while (rsP.next()) {
                        Permission p = new Permission();
                        p.setId(rsP.getLong("id"));
                        p.setName(rsP.getString("name"));
                        permissions.add(p);
                    }
                }
                // Gán danh sách quyền vào Role
                role.setPermissions(permissions);
                // --- KẾT THÚC LẤY PERMISSION ---

                u.setRole(role);
                System.out.println("--- Debug getting role event ---");
                System.out.println(role.getName());
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public User login(String email) {
        // 1. Câu lệnh SQL lấy User và Role
        final String sql = """
        SELECT 
            u.id,
            u.fullname,
            u.email,
            u.password_hash,
            r.id   AS role_id,
            r.name AS role_name,
                           r.is_active
        FROM users u
        LEFT JOIN roles r ON u.role_id = r.id
        WHERE u.email = ?
    """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setFullName(rs.getString("fullname"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));

                // 2. Khởi tạo đối tượng Role
                Role role = new Role();
                long roleId = rs.getLong("role_id");
                role.setId(roleId);
                role.setName(rs.getString("role_name"));
                role.setActive(rs.getBoolean("is_active"));

                // --- BẮT ĐẦU LẤY DANH SÁCH PERMISSION ---
                List<Permission> permissions = new ArrayList<>();
                final String sqlPerms = """
                SELECT p.id, p.name 
                FROM permissions p
                JOIN role_permissions rp ON p.id = rp.permission_id
                WHERE rp.role_id = ?
            """;

                try (PreparedStatement psP = conn.prepareStatement(sqlPerms)) {
                    psP.setLong(1, roleId);
                    ResultSet rsP = psP.executeQuery();
                    while (rsP.next()) {
                        Permission p = new Permission();
                        p.setId(rsP.getLong("id"));
                        p.setName(rsP.getString("name"));
                        permissions.add(p);
                    }
                }
                // Gán danh sách quyền vào Role
                role.setPermissions(permissions);
                // --- KẾT THÚC LẤY PERMISSION ---

                u.setRole(role);
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public List<Permission> getAllPermissions() {
        List<Permission> listPermission = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM laptop_wms.permissions;");

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString());) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setId(rs.getLong("id"));
                    permission.setName(rs.getString("name"));
                    permission.setDescription(rs.getString("description"));
                    listPermission.add(permission);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPermission;
    }

    public static void main(String[] args) {
        UserDAO d = new UserDAO();
        List<Permission> test = new ArrayList<Permission>();
        test = d.getAllPermissions();
        System.out.println(test);
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
}
