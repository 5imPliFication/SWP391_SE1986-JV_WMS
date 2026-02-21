package com.example.controller.purchase_request;

import com.example.enums.PurchaseRequestStatus;
import com.example.model.PurchaseRequest;
import com.example.model.User;
import com.example.service.PurchaseRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/purchase-request/list")
public class PurchaseRequestList extends HttpServlet {

    private PurchaseRequestService pr;

    @Override
    public void init() {
        pr = new PurchaseRequestService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = user.getRole().getName(); // MANAGER / STAFF / WAREHOUSE

        // ===== FILTER PARAM =====
        String requestCode = request.getParameter("requestCode");
        String status = request.getParameter("status");
        String createdDate = request.getParameter("createdDate");

        // ===== PAGINATION =====
        int pageNo = 1;
        int pageSize = 6;
        try {
            if (request.getParameter("pageNo") != null) {
                pageNo = Integer.parseInt(request.getParameter("pageNo"));
            }
        } catch (NumberFormatException ignored) {
        }

        // ===== LOAD DATA =====
        List<PurchaseRequest> list = pr.getList(
                user.getId(),
                role,
                requestCode,
                status,
                createdDate,
                pageNo,
                pageSize
        );

        int totalRecords = pr.count(
                user.getId(),
                role,
                requestCode,
                status,
                createdDate
        );

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // ===== SET ATTRIBUTE =====
        request.setAttribute("purchaseRequests", list);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statuses", PurchaseRequestStatus.values());
        request.setAttribute("showCreatedBy", "MANAGER".equalsIgnoreCase(role));
        request.setAttribute("isWarehouse", "WAREHOUSE".equalsIgnoreCase(role));

        request.getRequestDispatcher(
                "/WEB-INF/purchase_request/PurchaseRequestList.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
