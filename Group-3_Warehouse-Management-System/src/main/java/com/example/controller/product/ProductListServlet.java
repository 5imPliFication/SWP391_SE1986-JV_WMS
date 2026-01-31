package com.example.controller.product;

import com.example.model.*;
import com.example.service.*;
import com.example.util.EmailUtil;
import com.example.util.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

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
        String searchName = request.getParameter("searchName");
        String brandName = request.getParameter("brandName");
        String categoryName = request.getParameter("categoryName");

        // Using when the first time load this page (No param pageNo in URL)
        int pageNo = 1;
        // Get total record
        int totalProducts = productService.getTotalProducts(searchName, brandName, categoryName);
        // Count total pages
        int totalPages = (int) Math.ceil((double) totalProducts / UserConstant.PAGE_SIZE);

        if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").isEmpty()) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        List<Product> products = productService.findAll(searchName, brandName, categoryName, pageNo);
        List<Brand> brands = brandService.getActiveBrands();
        List<Category> categories = categoryService.getActiveCategories();
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.setAttribute("products", products);
        request.setAttribute("totalPages", totalPages);
        // Use to determine active page number for the first time
        request.setAttribute("pageNo", pageNo);
        request.getRequestDispatcher("/WEB-INF/product/product-list.jsp").forward(request, response);
    }
}