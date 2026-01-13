package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.util.EmailUtil;
import com.example.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init(){
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        //get unencrypted password from <form>
        User user = (User)request.getSession().getAttribute("user");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        // Check valid of current password
        String currentPasswordInDB = userDAO.getPassword(user.getEmail());
        boolean isValidCurrentPassword = PasswordUtil.checkPassword(currentPassword,currentPasswordInDB);

        if (isValidCurrentPassword){
            // Hash and save new password in database
            String newPasswordHashed = PasswordUtil.hashPassword(newPassword);
            userDAO.updatePassword(user.getEmail(),newPasswordHashed);

            // Kill session
            request.getSession().invalidate();

            // Re login
            response.sendRedirect(request.getContextPath() + "/login.jsp");

        } else {
            // Sends error message to change-password page
            request.setAttribute("error","Invalid current password");
            request.getRequestDispatcher("/change-password.jsp").forward(request, response);
        }
    }
}