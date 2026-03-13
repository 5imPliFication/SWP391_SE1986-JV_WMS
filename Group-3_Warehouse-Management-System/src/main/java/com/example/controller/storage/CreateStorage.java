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
@WebServlet(name = "CreateStorage", urlPatterns = {"/create-storage"})
public class CreateStorage extends HttpServlet {

    private StorageService s;

    @Override
    public void init() {
        s = new StorageService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String size = request.getParameter("size");

        boolean active = Boolean.parseBoolean(
                request.getParameter("active")
        );

        s.CreateStorage(size + "GB", active);

        response.sendRedirect(
                request.getContextPath()
                + "/specification/storage"
        );
    }

}
