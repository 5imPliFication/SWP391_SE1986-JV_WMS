package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.PermissionDAO;
import com.example.dao.RoleDAO;
import com.example.dao.UserActivityDAO;
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
@NoArgsConstructor
public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private UserActivityDAO userActivityDAO = new UserActivityDAO();
    private final PermissionDAO permissionDAO = new PermissionDAO();

    // create new user
    public String createUser(User user) {
        // check validate user
        String messageValidation = UserValidator.validateCreate(user);

        // if messageValidation has value -> return message validation
        if (messageValidation != null) {
            return messageValidation;
        }

        // check exist email
        if(userDAO.isExistEmail(user.getEmail())){
            return "Email exist, please input other email !!!";
        }

        // insert new user to DB + log
        boolean statusCreate = userDAO.insertUser(user);
        if (statusCreate) {
            userActivityDAO.initActivity(user.getId());
        }

        // if statusCreate = true -> return null
        // else return error
        return statusCreate ? null : "Can not create new user";
    }

    //get list user by name + pagination
    public List<User> getListUsers(String name, String typeSort, int pageNo) {
        return userDAO.findAll(name, typeSort, pageNo);
    }

    public User findUserById(long id) {
        return userDAO.findUserById(id);
    }

    // change status of user
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

            String currentHash = userDAO.getPassword(conn, email);
            if (!PasswordUtil.checkPassword(currentRawPassword, currentHash)) {
                return false;
            }

            String newHash = PasswordUtil.hashPassword(newRawPassword);
            boolean updated = userDAO.updatePassword(conn, email, newHash);

            if (updated) {
                User user = userDAO.findByEmail(conn, email);
                userActivityDAO.logPasswordChange(conn, user.getId());
            }

            return updated;

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
            try (Connection conn = DBConfig.getDataSource().getConnection()) {
                userDAO.getPassword(conn, email);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to load user by email: " + email, e);
            }
        }
        return null;
    }

    // Update User Infomation
    public boolean updateUserInformation(User user) {
        return userDAO.updateUserInformation(user);
    }

    public int getTotalUsers(String searchName) {
        return userDAO.countUsers(searchName);
    }
}
