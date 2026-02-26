package com.example.controller.audit;

import com.example.model.InventoryAudit;
import com.example.service.InventoryAuditService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/inventory-audits")
public class InventoryAuditListServlet extends HttpServlet {

    private InventoryAuditService inventoryAuditService;

    @Override
    public void init() {
        inventoryAuditService = new InventoryAuditService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String auditCode = request.getParameter("auditCode");
        String status = request.getParameter("status");

        // Using when the first time load this page (No param pageNo in URL)
        int pageNo = 1;
        // Get total record
        int totalInventoryAudits = inventoryAuditService.getTotalInventoryAudits(auditCode, status);
        // Count total pages
        int totalPages = (int) Math.ceil((double) totalInventoryAudits / AppConstants.PAGE_SIZE);

        if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").isEmpty()) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        List<InventoryAudit> inventoryAudits = inventoryAuditService.findAll(auditCode, status, pageNo);
        request.setAttribute("inventoryAudits", inventoryAudits);
        request.setAttribute("totalPages", totalPages);
        // Use to determine active page number for the first time
        request.setAttribute("pageNo", pageNo);
        request.getRequestDispatcher("/WEB-INF/audit/inventory-audit-list.jsp").forward(request, response);
    }

    // Change status of inventory audit
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long inventoryAuditId = Long.parseLong(request.getParameter("inventoryAuditId"));
        inventoryAuditService.cancelInventoryAudit(inventoryAuditId);

        // Redirect to the inventory audit list page after canceling
        String pageNo = request.getParameter("pageNo");
        String auditCode = request.getParameter("auditCode");
        String status = request.getParameter("status");

        response.sendRedirect(request.getContextPath() + "/inventory-audits?pageNo=" + pageNo + "&auditCode=" + auditCode + "&status=" + status);
    }
}