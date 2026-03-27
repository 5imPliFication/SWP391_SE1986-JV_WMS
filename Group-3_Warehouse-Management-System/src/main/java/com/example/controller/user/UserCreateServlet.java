package com.example.controller.user;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import com.example.validator.UserValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.example.util.PasswordUtil.hashPassword;
import java.util.List;

@WebServlet("/user/create")
public class UserCreateServlet extends HttpServlet {

    private UserService userService;
    private RoleService r;
    
    @Override
    public void init() {
        userService = new UserService();
        r = new RoleService();
    }

    // display screen create new user
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Role> roles = r.getAllRoles();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("/WEB-INF/user/user-create.jsp").forward(request, response);
    }

    // handle submit form create new user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        Role role = new Role();

        String rawPassword = request.getParameter("password");
        if(!UserValidator.validatePassword(rawPassword)){
            request.setAttribute("error", "Password must be at least 8 characters, contain 1 number, 1 capitalize character and 1 special character");
            request.getRequestDispatcher("/WEB-INF/user/user-create.jsp").forward(request, response);
        }
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPasswordHash(hashPassword(rawPassword));
        role.setId(Long.parseLong(request.getParameter("role")));
        user.setRole(role);

        String messageResponseError = userService.createUser(user);

        // if messageResponseError has value -> error
        if (messageResponseError != null) {
            request.setAttribute("error", messageResponseError);
            request.getRequestDispatcher("/WEB-INF/user/user-create.jsp").forward(request, response);
        } else {
            request.setAttribute("success", "Create new user successfully");
            request.getRequestDispatcher("/WEB-INF/user/user-create.jsp").forward(request, response);
        }

    }
}
