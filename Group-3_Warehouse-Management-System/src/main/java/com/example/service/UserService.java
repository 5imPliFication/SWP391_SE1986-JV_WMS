package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.PermissionDAO;
import com.example.dao.RoleDAO;
import com.example.dao.UserDAO;
import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;
import com.example.util.EmailUtil;
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

    public User findUserById(long id) {
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

    public boolean changePassword(String email,
                               String currentRawPassword,
                               String newRawPassword) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            System.out.println("Resetting password for email = [" + email + "]");
            String currentHash = userDAO.getPassword(conn, email);

            if (!PasswordUtil.checkPassword(currentRawPassword, currentHash)) {
                System.out.println("Password not matched");
                return false;
            }

            String newHash = PasswordUtil.hashPassword(newRawPassword);
            return userDAO.updatePassword(conn, email, newHash);
        } catch (SQLException e) {
            throw new RuntimeException("Password change failed", e);
        }
    }

    public boolean resetPasswordByEmail(String email) {

        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            User user = userDAO.findByEmail(conn, email);

            if (user == null || !user.isActive()) {
                return false;
            }

            // 1. Generate new password
            String newPassword = PasswordUtil.generateRandomPassword(10);

            // 2. Hash password
            String newHash = PasswordUtil.hashPassword(newPassword);

            // 3. Update DB
            userDAO.updatePassword(conn, email, newHash);

            // 4. Send email
            EmailUtil.sendEmail(email, newPassword);

            return true;

        } catch (Exception e) {
            throw new RuntimeException("Password reset failed", e);
        }
    }

    public String findPasswordByEmail(String email) {
        if (email != null) {
            try(Connection conn = DBConfig.getDataSource().getConnection()) {
                userDAO.getPassword(conn,email);
            }catch (SQLException e){
                throw new RuntimeException("Failed to load user by email: " + email, e);
            }
        }
        return null;
    }

    // Update User Infomation
    public boolean updateUserInformation(User user) {
        return userDAO.updateUserInformation(user);
    }

    // get list users by name
    public List<User> getUsersByName(String name) {
        List<User> listUsers;
        listUsers = userDAO.getUsersByName(name);
        return listUsers;
    }
}
