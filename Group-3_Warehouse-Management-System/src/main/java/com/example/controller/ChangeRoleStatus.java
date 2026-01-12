/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller;

import com.example.dao.RoleDAO;
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
@WebServlet(name = "ChangeRoleStatus", urlPatterns = {"/change_role_status"})
public class ChangeRoleStatus extends HttpServlet {

    private RoleDAO d;

    @Override
    public void init() {
        d = new RoleDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("roleId"));
        boolean test = !Boolean.parseBoolean(request.getParameter("currentStatus"));
        System.out.println("id = " + id);
        System.out.println("status " + test);
        d.changeRoleStatus(id, test);
        response.sendRedirect(request.getContextPath() + "/roles");
    }

}
