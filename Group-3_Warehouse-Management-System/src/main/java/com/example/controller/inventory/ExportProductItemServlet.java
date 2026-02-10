package com.example.controller.inventory;

import com.example.dao.InventoryDAO;
import com.example.service.InventoryService;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet({ "/export-products", "/export-product-items" })
public class ExportProductItemServlet extends HttpServlet {

    private final InventoryService inventoryService;

    public ExportProductItemServlet(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
            case "search":
                listOrders(request, response);
                break;
            case "detail":
                showDetail(request, response);
                break;
            default:
                listOrders(request, response);
                break;
        }
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String pageStr = request.getParameter("pageNo");

        int pageNo = 1;
        // get pageNo
        try {
            if (pageStr != null && !pageStr.isEmpty()) {
                pageNo = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        int pageSize = 10;
        Map<String, Object> result = inventoryService.getExportOrders(fromDate, toDate, pageNo, pageSize);

        request.setAttribute("exportOrders", result.get("orders"));
        request.setAttribute("totalPages", result.get("totalPages"));
        request.setAttribute("pageNo", pageNo);

        request.getRequestDispatcher("/WEB-INF/inventory/export-products.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/export-products");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
