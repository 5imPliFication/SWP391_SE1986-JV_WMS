package com.example.controller.category;

import com.example.model.Category;
import com.example.service.CategoryService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CategoryDetailServlet", urlPatterns = {"/category-detail"})
public class CategoryDetailServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("categories?status=error_id_missing");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            Category category = categoryService.getCategoryById(id);

            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/WEB-INF/category/category-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("categories?status=not_found");
            }
        } catch (NumberFormatException e) {
             response.sendRedirect("categories?status=error_invalid_id");
        }
    }
}
