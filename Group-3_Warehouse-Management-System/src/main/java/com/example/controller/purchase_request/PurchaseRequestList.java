package com.example.controller.purchase_request;

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
        User user = (session != null)
                ? (User) session.getAttribute("user")
                : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ===== ROLE CHECK =====
        boolean isManager = "MANAGER".equalsIgnoreCase(user.getRole().getName());

        // ===== Dùng cho JSP ẩn/hiện cột =====
        request.setAttribute("showCreatedBy", isManager);

        // ===== FILTER PARAMS =====
        String requestCode = request.getParameter("requestCode");
        String status = request.getParameter("status");
        String createdDate = request.getParameter("createdDate");

        // ===== LOAD LIST =====
        List<PurchaseRequest> list = pr.getList(
                user.getId(),
                isManager,
                requestCode,
                status,
                createdDate
        );
        request.setAttribute("purchaseRequests", list);

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
