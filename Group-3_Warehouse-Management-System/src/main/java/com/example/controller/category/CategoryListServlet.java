package com.example.controller.category;

import com.example.model.Category;
import com.example.service.CategoryService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CategoryListServlet", urlPatterns = {"/categories"})
public class CategoryListServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String searchName = request.getParameter("searchName");
            String statusParam = request.getParameter("statusFilter");

            Integer isActive = null;
            if (statusParam != null && !statusParam.isEmpty()) {
                isActive = Integer.parseInt(statusParam);
            }

            List<Category> categories = categoryService.searchCategories(searchName, isActive);
            request.setAttribute("categories", categories);
            request.setAttribute("searchName", searchName);
            request.setAttribute("statusFilter", statusParam);
            request.getRequestDispatcher("/WEB-INF/category/category-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving categories");
        }
    }
}
