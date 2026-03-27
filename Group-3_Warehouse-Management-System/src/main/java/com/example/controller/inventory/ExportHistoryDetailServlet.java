package com.example.controller.inventory;

import com.example.dto.ExportDTO;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/export-history/detail")
public class ExportHistoryDetailServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("orderId");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/export-history");
            return;
        }

        Long orderId = Long.parseLong(idStr);
        ExportDTO exportDTO = orderService.getExportHistoryDetail(orderId);

        if (exportDTO == null) {
            request.setAttribute("error", "Order not found or not in completed status.");
            request.getRequestDispatcher("/WEB-INF/inventory/export-history.jsp").forward(request, response);
            return;
        }

        request.setAttribute("order", exportDTO);
        request.getRequestDispatcher("/WEB-INF/inventory/export-history-detail.jsp").forward(request, response);
    }
}
