package com.example.controller.category;


import com.example.service.CategoryService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "CategoryCreateServlet", urlPatterns = {"/create-category"})
public class CategoryCreateServlet extends HttpServlet {


    private CategoryService categoryService;


    @Override
    public void init() {
        categoryService = new CategoryService();
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


            categoryService.createCategory(name, description, isActive);


            // Redirect to list page (assuming /categories exists, otherwise to create with success)
            response.sendRedirect("categories?status=add_success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("create-category?status=add_error");
        }
    }
}
