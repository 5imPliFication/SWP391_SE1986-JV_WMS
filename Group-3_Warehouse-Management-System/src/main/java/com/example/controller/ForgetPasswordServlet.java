package com.example.controller;

import com.example.dao.UserDAO;
import com.example.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/forget_password")
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
        String emailTo = request.getParameter("email");

        // Check user existed with email
        if(!userDAO.existsByEmail(emailTo)){
            request.getRequestDispatcher("/forget-password-error.jsp").forward(request,response);
        } else {
            // Create new password
            String newPassword = PasswordRandomUtil.generateRandomPassword(10);

            // Send email
            EmailUtil.sendEmail(emailTo,newPassword);

            // Hash password (Bcrypt) and save in database

            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }

    }
}