package com.example.controller.category;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.CategoryService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CategoryCreateServlet", urlPatterns = {"/create-category"})
public class CategoryCreateServlet extends HttpServlet {

    private CategoryService categoryService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        categoryService = new CategoryService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/category/create-category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            boolean isActive = request.getParameter("status") != null && request.getParameter("status").equals("true");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            categoryService.createCategory(name, description, isActive);
            activityLogService.log(user, "Create Category");

            // Redirect to list page (assuming /categories exists, otherwise to create with success)
            response.sendRedirect("categories?status=add_success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("create-category?status=add_error");
        }
    }
}
