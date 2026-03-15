/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.brand;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.BrandService;
import java.io.IOException;
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
@WebServlet(urlPatterns = {"/change-status"})
public class ChangeBrandStatus extends HttpServlet {

    private BrandService b;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        b = new BrandService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("brandId"));
        boolean isActive = Boolean.parseBoolean(request.getParameter("status"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        b.updateStatus(id, !isActive);

        if (isActive) {
            activityLogService.log(user, "Deactive brand");
        } else {
            activityLogService.log(user, "Active brand");
        }
        response.sendRedirect("brands?status=update_success");

    }

}
