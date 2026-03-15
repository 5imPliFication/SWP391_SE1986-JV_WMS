/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.size;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.SizeService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author PC
 */
@WebServlet(name = "CreateSize", urlPatterns = {"/create-size"})
public class CreateSize extends HttpServlet {

    private SizeService s;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        s = new SizeService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String size = request.getParameter("size");

        boolean active = Boolean.parseBoolean(
                request.getParameter("active")
        );

        s.CreateSize(size + " inch", active);
        activityLogService.log(user, "Create size");

        response.sendRedirect(
                request.getContextPath()
                + "/specification/size"
        );
    }

}
