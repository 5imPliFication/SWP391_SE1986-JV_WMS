package com.example.controller;

import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = "/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole().getName();

        if ("Admin".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            return;
        } else if ("Manager".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/manager-dashboard");
            return;
        } else if ("Salesman".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/salesman/dashboard");
            return;
        } else if ("Warehouse".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/warehouse/dashboard");
            return;
        }

        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
