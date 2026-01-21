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
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> listUsers;

        String searchName = request.getParameter("searchName");

        //if searchName == null or blank
        if (searchName == null || searchName.trim().isEmpty()) {
            listUsers = userService.getListUsers();
            request.setAttribute("userList", listUsers);
            request.getRequestDispatcher("/WEB-INF/user/user-list.jsp").forward(request, response);
        } else {
            listUsers = userService.getUsersByName(searchName);
            request.setAttribute("userList", listUsers);
            request.getRequestDispatcher("/WEB-INF/user/user-list.jsp").forward(request, response);
        }

    }
}
