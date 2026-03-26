/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.model;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.ModelService;
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
@WebServlet(name = "CreateModel", urlPatterns = {"/create-model"})
public class CreateModel extends HttpServlet {

    private ModelService m;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        m = new ModelService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        long brandId = Long.parseLong(
                request.getParameter("brandId")
        );

        boolean active = Boolean.parseBoolean(
                request.getParameter("active")
        );

        if (m.modelExists(name, brandId)) {
            session.setAttribute("flashModelCreateError", "Đã tồn tại");
            response.sendRedirect(request.getContextPath() + "/specification/model");
            return;
        }

        m.CreateModel(name, brandId, active);
        activityLogService.log(user, "Crete model");

        response.sendRedirect(
                request.getContextPath()
                + "/specification/model"
        );
    }

}
