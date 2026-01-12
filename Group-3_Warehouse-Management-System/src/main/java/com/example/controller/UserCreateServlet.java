package com.example.controller;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user-create")
public class UserCreateServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user-create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        Role role = new Role();
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPasswordHash(request.getParameter("password"));
        role.setId(Long.parseLong(request.getParameter("role")));
        user.setRole(role);

        System.out.println(request.getParameter("email"));
        String messageError = userService.createUser(user);

        if (messageError != null) {
            request.setAttribute("error", messageError);
            request.getRequestDispatcher("/user-create.jsp").forward(request, response);
        } else {
            request.setAttribute("success", "Create new user successfully");
            request.getRequestDispatcher("/user-create.jsp").forward(request, response);
        }

    }
}
