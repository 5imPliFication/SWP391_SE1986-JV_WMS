/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.size;

import com.example.service.SizeService;
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
@WebServlet(name = "ActiveSize", urlPatterns = {"/active-size"})
public class ActiveSize extends HttpServlet {

    private SizeService s;

    @Override
    public void init() {
        s = new SizeService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        boolean active = Boolean.parseBoolean(request.getParameter("active"));

        s.changeSizeStatus(id, active);

        response.sendRedirect(request.getContextPath() + "/specification/size");
    }

}
