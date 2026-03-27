package com.example.controller.auth;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.UserService;
import com.example.validator.UserValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    private UserService userService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        userService = new UserService();
        activityLogService = new ActivityLogService();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/auth/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        boolean success;
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Set user to request for displaying info
        request.setAttribute("user", user);

        System.out.println(user.getEmail());
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        
        // Validate new password
        if (!UserValidator.validatePassword(newPassword)) {
            request.setAttribute("error", "Password must be at least 8 characters, contain 1 number, 1 capitalize character and 1 special character");
            request.getRequestDispatcher("/WEB-INF/auth/change-password.jsp").forward(request, response);
            return;
        }
        
        success = userService.changePassword(
                user.getEmail().trim().toLowerCase(),
                currentPassword,
                newPassword
        );
        activityLogService.log(user, "Change password!");
        if (success) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.setAttribute("error", "Invalid current password");
            request.getRequestDispatcher("/WEB-INF/auth/change-password.jsp")
                    .forward(request, response);
        }
    }
}
