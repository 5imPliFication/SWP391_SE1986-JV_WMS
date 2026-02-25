package com.example.controller.inventory;

import com.example.dto.OrderDTO;
import com.example.service.InventoryService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/inventory/export")
public class ExportProductItemServlet extends HttpServlet {

    private InventoryService inventoryService;

    @Override
    public void init() throws ServletException {
        inventoryService = new InventoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "search";
        }
        // handle action
        if(action.equals("search")){
            handleSearch(request, response);
        }
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String status = request.getParameter("status");
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

        Map<String, Object> result = inventoryService.getExportOrders(name, fromDate, toDate, status, pageNo);

        request.setAttribute("name", name);
        request.setAttribute("exportOrders", result.get("orders"));
        request.setAttribute("totalPages", result.get("totalPages"));
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.setAttribute("status", status);
        request.setAttribute("pageNo", pageNo);

        request.getRequestDispatcher("/WEB-INF/inventory/export-products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
