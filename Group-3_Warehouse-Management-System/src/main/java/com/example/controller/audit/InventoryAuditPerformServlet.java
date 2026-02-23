package com.example.controller.audit;

import com.example.model.InventoryAudit;
import com.example.model.InventoryAuditItem;
import com.example.service.InventoryAuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/inventory-audits/perform")
public class InventoryAuditPerformServlet extends HttpServlet {

    private InventoryAuditService inventoryAuditService;

    @Override
    public void init() {
        inventoryAuditService = new InventoryAuditService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long inventoryAuditId = Long.parseLong(request.getParameter("inventoryAuditId"));
        InventoryAudit inventoryAudit = inventoryAuditService.getInventoryAuditById(inventoryAuditId);
        List<InventoryAuditItem> inventoryAuditItems = inventoryAuditService.getInventoryAuditItemsByAuditId(inventoryAuditId);
        request.setAttribute("inventoryAudit", inventoryAudit);
        request.setAttribute("inventoryAuditItems", inventoryAuditItems);

        request.getRequestDispatcher("/WEB-INF/audit/inventory-audit-perform.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long inventoryAuditId = Long.parseLong(request.getParameter("inventoryAuditId"));
        String[] inventoryAuditItemIds = request.getParameterValues("inventoryAuditItemId");
        String[] physicalQuantities = request.getParameterValues("physicalQuantity");
        String[] reasons = request.getParameterValues("reason");

        // Call service to perform inventory audit
        try {
            inventoryAuditService.performInventoryAudit(inventoryAuditId, inventoryAuditItemIds, physicalQuantities, reasons);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Back exactly previous page
        String pageNo = request.getParameter("pageNo");
        String auditCode = request.getParameter("auditCode");
        String status = request.getParameter("status");

        response.sendRedirect(request.getContextPath() + "/inventory-audits?pageNo=" + pageNo + "&auditCode=" + auditCode + "&status=" + status);
    }
}