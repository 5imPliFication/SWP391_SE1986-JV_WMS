package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/forget-password")
public class ForgetPasswordServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init(){
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/forget-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        User user = userDAO.existedByEmail(email);

        // Check user existed with email
        if(user==null || !user.isActive()){
            try {
                request.getRequestDispatcher("/forget-password-error.jsp").forward(request, response);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        } else {
            // Create new password
            String newPassword = PasswordUtil.generateRandomPassword(10);

            // Send email
            EmailUtil.sendEmail(email,newPassword);

            // Hash password (Bcrypt) and save in database
            String passwordHashed = PasswordUtil.hashPassword(newPassword);
            userDAO.updatePassword(email,passwordHashed);

            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }

    }
}