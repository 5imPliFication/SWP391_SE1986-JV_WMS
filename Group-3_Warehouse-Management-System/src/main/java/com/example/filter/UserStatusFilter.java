package com.example.filter;


import com.example.dao.UserDAO;
import com.example.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class UserStatusFilter implements Filter {

    private UserDAO userDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // allow path
        String path = request.getRequestURI();
        if (path.equals("/login")
                || path.equals("/login.jsp")
                || path.equals("/forget-password")
                || path.startsWith("/static/css/")) {

            chain.doFilter(request, response);
            return;
        }
        // get current session, dont create new session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("/login");
            return;
        }

        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            response.sendRedirect("/login");
            return;
        }

        User user = userDAO.findUserById(userSession.getId());
        if (user == null || !user.isActive()) {
            session.invalidate();
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Account inactive");
            return;
        }

        chain.doFilter(request, response);
    }

}
