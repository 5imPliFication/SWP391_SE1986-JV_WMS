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

@WebServlet("/inventory/alert")
public class OutOfStockAlertServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get action
        String action = request.getParameter("action");

        if (action == null) {
            action = "search";
        }

        if("search".equals(action)) {
            handleSearch(request);
        }

        request.getRequestDispatcher("/WEB-INF/inventory/out-of-stock-alert.jsp").forward(request, response);
    }

    private void handleSearch(HttpServletRequest request) {
        String name = request.getParameter("name");
        List<ProductDTO> productList = productService.getOutOfStockAlertProducts(name);

        request.setAttribute("name", name);
        request.setAttribute("products", productList);
    }
}
