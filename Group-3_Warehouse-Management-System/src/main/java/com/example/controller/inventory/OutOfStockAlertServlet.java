package com.example.controller.inventory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import com.example.dto.ProductDTO;
import com.example.service.ProductService;

@WebServlet("/out-of-stock-alert")
public class OutOfStockAlertServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<ProductDTO> productList = productService.getOutOfStockAlertProducts();
        request.setAttribute("productList", productList);

        request.getRequestDispatcher("/WEB-INF/inventory/out-of-stock-alert.jsp").forward(request, response);
    }
}
