package com.example.controller.user;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.example.util.PasswordUtil.hashPassword;

@WebServlet("/user-create")
public class UserCreateServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    // display screen create new user
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/user/user-create.jsp").forward(request, response);
    }

    // handle submit form create new user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        Role role = new Role();
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPasswordHash(hashPassword(request.getParameter("password")));
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
