/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.storage;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.StorageService;
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
@WebServlet(name = "CreateStorage", urlPatterns = {"/create-storage"})
public class CreateStorage extends HttpServlet {

    private StorageService s;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        s = new StorageService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String size = request.getParameter("size");

        boolean active = Boolean.parseBoolean(
                request.getParameter("active")
        );
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String sizeValue = size + "GB";
        if (s.storageExists(sizeValue)) {
            session.setAttribute("flashStorageCreateError", "Đã tồn tại");
            response.sendRedirect(request.getContextPath() + "/specification/storage");
            return;
        }

        s.CreateStorage(sizeValue, active);
        activityLogService.log(user, "Create storage");

        response.sendRedirect(
                request.getContextPath()
                + "/specification/storage"
        );
    }

}
