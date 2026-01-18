package com.example.controller.user;

import com.example.model.User;
import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/user-list")
public class UserListServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(){
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> listUsers = userService.getListUsers();

        request.setAttribute("userList", listUsers);
        request.getRequestDispatcher("/user-list.jsp").forward(request,response);

    }
}
