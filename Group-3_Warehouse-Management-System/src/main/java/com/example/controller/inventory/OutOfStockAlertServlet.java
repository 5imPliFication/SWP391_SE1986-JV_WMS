package com.example.controller.inventory;

import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

        if ("search".equals(action)) {
            handleSearch(request);
        }

        request.getRequestDispatcher("/WEB-INF/inventory/out-of-stock-alert.jsp").forward(request, response);
    }

    private void handleSearch(HttpServletRequest request) {
        String name = request.getParameter("name");
        String pageStr = request.getParameter("pageNo");

        int pageNo = AppConstants.DEFAULT_PAGE_NO;
        // get pageNo
        try {
            if (pageStr != null && !pageStr.isEmpty()) {
                pageNo = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            pageNo = AppConstants.DEFAULT_PAGE_NO;
        }
        Map<String, Object> result = productService.getOutOfStockAlertProducts(name, pageNo);

        request.setAttribute("name", name);
        request.setAttribute("products", result.get("products"));
        request.setAttribute("totalPages", result.get("totalPages"));
        request.setAttribute("pageNo", pageNo);
    }
}
