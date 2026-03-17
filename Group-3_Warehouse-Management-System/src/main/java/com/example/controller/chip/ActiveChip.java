/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.chip;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.ChipService;
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
@WebServlet(name = "ActiveChip", urlPatterns = {"/active-chip"})
public class ActiveChip extends HttpServlet {

    private ChipService c;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        c = new ChipService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        boolean active = Boolean.parseBoolean(request.getParameter("active"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        c.changeChipStatus(id, active);
        if (active) {
            activityLogService.log(user, "Active chip");
        } else {
            activityLogService.log(user, "Deactive chip");
        }
        response.sendRedirect(request.getContextPath() + "/specification/chip");
    }

}
