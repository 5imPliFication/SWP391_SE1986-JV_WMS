package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/forget-password")
public class ForgetPasswordServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/forget-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");

        boolean success = userService.resetPasswordByEmail(email);

        if (!success) {
            request.getRequestDispatcher("/forget-password-error.jsp")
                    .forward(request, response);
            return;
        }

        request.getRequestDispatcher("/login.jsp")
                .forward(request, response);
    }
}