package com.example.controller.user;

import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/change-user-status")
public class UserChangeStatusServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(){
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("userId"));
        boolean messageStatus = userService.changeStatus(id);

        request.setAttribute("messageStatus", messageStatus);
        request.setAttribute("messageSuccess", "Change status successfully");
        request.setAttribute("messageFail", "Change status fail");
        response.sendRedirect(request.getContextPath() + "/user-list");
    }
}
