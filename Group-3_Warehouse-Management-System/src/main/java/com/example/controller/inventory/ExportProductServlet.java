package com.example.controller.inventory;

import com.example.dto.ExportDTO;
import com.example.dto.ExportItemDTO;
import com.example.model.User;
import com.example.service.InventoryService;
import com.example.service.ActivityLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/export")
public class ExportProductServlet extends HttpServlet {

    private InventoryService inventoryService;

    @Override
    public void init() {
        inventoryService = new InventoryService();
    }

    // display export order details and form to assign serials
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // get order ID
            Long orderId = Long.parseLong(request.getParameter("orderId"));

            // get order information
            ExportDTO order = inventoryService.getExportOrder(orderId);

            // set information for order and forward to JSP
            HttpSession session = request.getSession();
            session.setAttribute("order", order);

            request.getRequestDispatcher("/WEB-INF/inventory/export-product.jsp").forward(request, response);
        } catch (Exception e) {
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        try {
            Long orderId = Long.parseLong(request.getParameter("orderId"));

            // get export order details to process serials
            HttpSession session = request.getSession();
            ExportDTO exportOrder = (ExportDTO) session.getAttribute("order");

            String[] serials = request.getParameterValues("serial");
            
            // Assign serials to order items (validates and maps them)
            inventoryService.assignSerialForExportProduct(exportOrder, serials);

            response.sendRedirect(request.getContextPath() + "/warehouse/order/detail?id=" + orderId);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/inventory/export-product.jsp").forward(request, response);
        } catch (Exception e) {
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
