package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user")
public class UserDetailServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(){
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = userService.findUserById(Integer.parseInt(request.getParameter("id")));

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user-detail.jsp").forward(request, response);
    }
}
