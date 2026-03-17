package com.example.controller.category;

import com.example.model.Category;
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

@WebServlet(name = "CategoryUpdateServlet", urlPatterns = {"/update-category"})
public class CategoryUpdateServlet extends HttpServlet {

    private CategoryService categoryService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        categoryService = new CategoryService();
        activityLogService = new ActivityLogService();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing category ID");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            Category category = categoryService.getCategoryById(id);
            if (category == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
                return;
            }
            request.setAttribute("category", category);
            request.getRequestDispatcher("/WEB-INF/category/update-category.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String statusStr = request.getParameter("status");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing category ID");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            int isActive = Boolean.parseBoolean(statusStr) ? 1 : 0;

            Category category = new Category();
            category.setId(id);
            category.setName(name);
            category.setDescription(description);
            category.setIsActive(isActive);

            categoryService.updateCategory(category);

            activityLogService.log(user, "Update Category");

            response.sendRedirect("categories?status=updated");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid Data");
            request.getRequestDispatcher("/WEB-INF/category/update-category.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error updating category", e);
        }
    }
}
