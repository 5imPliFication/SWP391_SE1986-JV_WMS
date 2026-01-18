package com.example.controller.user;

import com.example.dao.UserDAO;
import com.example.dao.RoleDAO;
import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/user")
public class UserDetailServlet extends HttpServlet {

    private UserService userService;
    private UserDAO userDAO;
    private RoleDAO roleDAO;

    @Override
    public void init() {
        userService = new UserService();
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = userService.findUserById(Integer.parseInt(request.getParameter("id")));
        List<Role> roles = roleDAO.findAll();
        request.setAttribute("roles", roles);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/user/user-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            Long roleId = Long.parseLong(request.getParameter("roleId"));
            boolean active = Boolean.parseBoolean(request.getParameter("active"));

            User user = new User();
            user.setId(id);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setActive(active);

            Role role = new Role();
            role.setId(roleId);
            user.setRole(role);

            userDAO.updateUserInformation(user);

            response.sendRedirect(request.getContextPath() + "/user-list?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user?id=" + request.getParameter("id") + "&error=1");
        }
    }
}
