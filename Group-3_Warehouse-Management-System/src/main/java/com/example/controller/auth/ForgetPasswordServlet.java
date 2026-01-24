package com.example.controller.auth;

import com.example.model.User;
import com.example.service.PasswordResetService;
import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Using when User forget password and request new password
@WebServlet("/forget-password")
public class ForgetPasswordServlet extends HttpServlet {
    private UserService userService;
    private PasswordResetService passwordResetService;

    @Override
    public void init() {
        userService = new UserService();
        passwordResetService = new PasswordResetService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/auth/forget-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");

        // Check user existed by email
        User user = userService.getUserByEmail(email);
        if (user == null) {
            request.setAttribute("error", "Your email does not exist in the system!");
            request.getRequestDispatcher("/WEB-INF/auth/forget-password.jsp").forward(request, response);
        } else {
            // Create a new password reset request
            passwordResetService.createPasswordResetRequest(user.getId());

            request.setAttribute("status","Your reset password request was sent successfully!");
            request.getRequestDispatcher("/WEB-INF/auth/forget-password.jsp").forward(request, response);
        }


    }
}