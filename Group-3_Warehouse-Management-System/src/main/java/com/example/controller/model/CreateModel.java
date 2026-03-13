/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.model;

import com.example.service.ModelService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author PC
 */
@WebServlet(name = "CreateModel", urlPatterns = {"/create-model"})
public class CreateModel extends HttpServlet {

    private ModelService m;

    @Override
    public void init() {
        m = new ModelService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");

        long brandId = Long.parseLong(
                request.getParameter("brandId")
        );

        boolean active = Boolean.parseBoolean(
                request.getParameter("active")
        );

        m.CreateModel(name, brandId, active);

        response.sendRedirect(
                request.getContextPath()
                + "/specification/model"
        );
    }

}
