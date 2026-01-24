package com.example.controller.user;

import com.example.model.PasswordReset;
import com.example.model.User;
import com.example.service.PasswordResetService;
import com.example.service.UserService;
import com.example.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

// Admin Approve or Cancelled
@WebServlet("/admin/password-reset")
public class HandleResetPasswordServlet extends HttpServlet {
    private UserService userService;
    private PasswordResetService passwordResetService;

    @Override
    public void init() {
        passwordResetService = new PasswordResetService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String searchName = request.getParameter("searchName");
        String status = request.getParameter("status");
        List<PasswordReset> passwordResetList = passwordResetService.findAll(searchName, status);
        request.setAttribute("passwordResetList", passwordResetList);
        request.getRequestDispatcher("/WEB-INF/user/user-reset-password-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long passwordResetId = Long.parseLong(request.getParameter("passwordResetId"));
        String emailTo = request.getParameter("userEmail");
        String action = request.getParameter("action");

        if (action.equals("Reject")) {
            passwordResetService.updateStatus(passwordResetId, "REJECTED");
        } else if (action.equals("Approve")) {
            // Update Status
            passwordResetService.updateStatus(passwordResetId, "APPROVED");

            // Create a new password and send to user through email
            userService.resetPasswordByEmail(emailTo);
        }
        // Keep current filter
        String searchName = request.getParameter("searchName");
        String status = request.getParameter("status");

        response.sendRedirect(request.getContextPath()
                + "/admin/password-reset?searchName=" + (searchName != null ? searchName : "")
                + "&status=" + (status != null ? status : "")
        );
    }
}