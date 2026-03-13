/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.storage;

import com.example.service.StorageService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author PC
 */
@WebServlet(name = "ActiveStorage", urlPatterns = {"/active-storage"})
public class ActiveStorage extends HttpServlet {

    private StorageService s;

    @Override
    public void init() {
        s = new StorageService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        boolean active = Boolean.parseBoolean(request.getParameter("active"));

        s.changeStorageStatus(id, active);

        response.sendRedirect(request.getContextPath() + "/specification/storage");
    }

}
