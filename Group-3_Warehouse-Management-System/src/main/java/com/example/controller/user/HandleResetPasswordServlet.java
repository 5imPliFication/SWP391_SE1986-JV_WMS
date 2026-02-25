package com.example.controller.user;

import com.example.model.PasswordReset;
import com.example.service.PasswordResetService;
import com.example.service.UserService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        // Using when the first time load this page (No param pageNo in URL)
        int pageNo = 1;
        // Get total record
        int totalPasswordResetRequest = passwordResetService.getTotalRequest(searchName, status);
        System.out.println("Total Password Reset Request: " + totalPasswordResetRequest);
        // Count total pages
        int totalPages = (int)Math.ceil((double) totalPasswordResetRequest/ AppConstants.PAGE_SIZE);
        System.out.println("Total pages: " + totalPages);

        if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").isEmpty()) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        List<PasswordReset> passwordResetList = passwordResetService.findAll(searchName, status, pageNo);
        request.setAttribute("passwordResetList", passwordResetList);
        request.setAttribute("totalPages", totalPages);
        // Use to determine active page number for the first time
        request.setAttribute("pageNo", pageNo);
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
        String pageNo = request.getParameter("pageNo");

        response.sendRedirect(request.getContextPath()
                + "/admin/password-reset?"
                + "pageNo=" + (pageNo != null ? pageNo : "")
                + "&searchName=" + (searchName != null ? searchName : "")
                + "&status=" + (status != null ? status : "")
        );
    }
}