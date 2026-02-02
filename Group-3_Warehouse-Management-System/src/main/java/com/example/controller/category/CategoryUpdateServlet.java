package com.example.controller.category;

import com.example.model.Category;
import com.example.service.CategoryService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CategoryUpdateServlet", urlPatterns = {"/update-category"})
public class CategoryUpdateServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() {
        categoryService = new CategoryService();
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

        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing category ID");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            boolean isActive = Boolean.parseBoolean(statusStr);

            Category category = new Category();
            category.setId(id);
            category.setName(name);
            category.setDescription(description);
            category.setIsActive(isActive);

            categoryService.updateCategory(category);
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
