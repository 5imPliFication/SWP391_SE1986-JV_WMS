package com.example.controller.order;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/salesman/order/create")
public class CreateOrderServlet extends HttpServlet {
    private OrderService orderService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        orderService = new OrderService();
         activityLogService = new ActivityLogService();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Check if user has Salesman role
        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Salesman role required.");
            return;
        }

        int orderId = orderService.createDraftOrder("Sample name", "", "", user.getId());
        activityLogService.log(user, "Create order");
        resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String customerName = req.getParameter("customerName");
        String customerPhone = req.getParameter("customerPhone");
        String note = req.getParameter("note");

        if (customerName == null || customerName.trim().isEmpty()) {
            customerName = "Walk-in Customer";
        }

        int orderId = orderService.createDraftOrder(customerName, customerPhone, note, user.getId());
        activityLogService.log(user, "Create order");

        resp.sendRedirect(req.getContextPath()
                + "/salesman/order/detail?id=" + orderId);
    }
}
