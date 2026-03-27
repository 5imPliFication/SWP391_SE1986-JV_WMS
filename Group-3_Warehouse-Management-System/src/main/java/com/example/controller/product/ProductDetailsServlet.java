package com.example.controller.product;

import com.example.model.*;
import com.example.service.*;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/products/details")
public class ProductDetailsServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long productId = Long.parseLong(request.getParameter("productId"));

        Product product = productService.findProductById(productId);
        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/product/product-details.jsp").forward(request, response);
    }
}