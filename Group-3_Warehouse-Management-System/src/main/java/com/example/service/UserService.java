package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.PermissionDAO;
import com.example.dao.RoleDAO;
import com.example.dao.UserDAO;
import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;
import com.example.util.PasswordUtil;
import com.example.validator.UserValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.example.util.PasswordUtil.hashPassword;

@AllArgsConstructor
@NoArgsConstructor
public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();
    private final PermissionDAO permissionDAO = new PermissionDAO();


    public String createUser(User user) {
        // check validate user
        String error = UserValidator.validateCreate(user);

        // if error -> return message error
        if(error != null){
            return error;
        }

        // check exist email
        if(getUserByEmail(user.getEmail())!=null){
            return "Email exist, please input other email !!!";
        };

        // insert new user to DB
        boolean statusCreate = userDAO.insertUser(user);
        // return
        return statusCreate ? null : "Can not create new user";
    }

    public List<User> getListUsers() {
        return userDAO.findAll();
    }

    public User findUserById(int id) {
        return userDAO.findUserById(id);
    }

    public boolean changeStatus(int id) {
        // get current status from DB
        boolean currentStatus = userDAO.getCurrentStatus(id);
        // change status
        return userDAO.changeStatus(id, !currentStatus);
    }

    public User getUserByEmail(String email) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            // 1. Load user + role
            User user = userDAO.findByEmail(conn, email);
            if (user == null) {
                return null;
            }

            // 2. Load permissions for role
            Role role = user.getRole();
            if (role != null && role.getId() > 0) {
                List<Permission> permissions =
                        permissionDAO.findByRoleId(conn, role.getId());
                role.setPermissions(permissions);
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load user by email", e);
        }
    }

    public void changePassword(String email, String newPassword) {
        // 1. Validate input (business rule)
        if (email == null || newPassword == null || newPassword.isBlank()) {
            System.out.println("Invalid account detail");
        }

        // 2. Hash password
        String passwordHash = hashPassword(newPassword);

        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            // 3. Execute update
            userDAO.updatePassword(conn, email, passwordHash);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update password for email: " + email, e);
        }
    }
}
