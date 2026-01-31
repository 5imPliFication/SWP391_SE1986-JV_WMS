package com.example.controller.product;

import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Product;
import com.example.service.BrandService;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.util.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/products/add")
public class ProductAddServlet extends HttpServlet {

    private ProductService productService;
    private BrandService brandService;
    private CategoryService categoryService;

    @Override
    public void init() {
        productService = new ProductService();
        brandService = new BrandService();
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Brand> brands = brandService.getActiveBrands();
        List<Category> categories = categoryService.getActiveCategories();

        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/product/product-add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productName = request.getParameter("productName");
        String description = request.getParameter("productDescription");
        String imgUrl = request.getParameter("imgUrl");
        long brandId = Long.parseLong(request.getParameter("brandId"));
        long categoryId = Long.parseLong(request.getParameter("categoryId"));

        if(productService.addProduct(productName, description, imgUrl, brandId, categoryId)) {
            request.getSession().setAttribute("success-message", "Product added successfully");
        } else {
            request.getSession().setAttribute("error-message", "Product add failed");
        }
        response.sendRedirect("/products");
    }
}