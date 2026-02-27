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
import java.util.List;

@WebServlet("/inventory-audits/detail")
public class InventoryAuditDetailServlet extends HttpServlet {

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

        request.getRequestDispatcher("/WEB-INF/audit/inventory-audit-detail.jsp").forward(request, response);
    }
}