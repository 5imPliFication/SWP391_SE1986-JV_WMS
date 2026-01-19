package com.example.controller.auth;

import com.example.model.User;
import com.example.service.UserService;
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

    @Override
    public void init() {
        userService = new UserService();
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
        success = userService.changePassword(
                user.getEmail().trim().toLowerCase(),
                currentPassword,
                newPassword
        );
        if(success){
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }else {
            request.setAttribute("error", "Invalid current password");
            request.getRequestDispatcher("/WEB-INF/auth/change-password.jsp")
                    .forward(request, response);
        }
    }
}