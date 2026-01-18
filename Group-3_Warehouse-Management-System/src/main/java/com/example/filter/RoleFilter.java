/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.example.filter;

import com.example.dao.PermissionDAO.PermissionChecker;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author PC
 */
@WebFilter("/*")
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String servletPath = request.getServletPath();

        // 1. Cho phép truy cập Login và các tài nguyên tĩnh (CSS/JS)
        if (servletPath.equals("/login")
                || servletPath.equals("/forget-password")
                || servletPath.contains("/common/")
                || servletPath.contains("/static/")
                || servletPath.endsWith(".css")
                || servletPath.endsWith(".js")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Kiểm tra Đăng nhập
        com.example.model.User user = (session != null) ? (com.example.model.User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String requiredPermission = PermissionChecker.getRequiredPermission(servletPath);

        if (requiredPermission != null) {
            // Lấy list String từ session
            List<String> userPermissions = (List<String>) session.getAttribute("userPermissions");

            if (userPermissions == null || !userPermissions.contains(requiredPermission)) {
                response.sendError(403, "Access Denied!");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
