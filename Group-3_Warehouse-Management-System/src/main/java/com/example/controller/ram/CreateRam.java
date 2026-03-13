/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.ram;

import com.example.service.RamService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CreateRam", urlPatterns = {"/create-ram"})
public class CreateRam extends HttpServlet {

    private RamService r;

    @Override
    public void init() {
        r = new RamService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String size = request.getParameter("size");

        boolean active = Boolean.parseBoolean(
                request.getParameter("active")
        );

        r.CreateRam(size + "GB", active);

        response.sendRedirect(
                request.getContextPath()
                + "/specification/ram"
        );
    }
}
