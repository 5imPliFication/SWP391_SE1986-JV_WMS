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

@WebServlet("/warehouse/order/process")
public class ProcessOrderServlet extends HttpServlet {

    private OrderService orderService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        orderService = new OrderService();
        activityLogService = new ActivityLogService();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !"Warehouse".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            orderService.startProcessing(orderId, user.getId());
            activityLogService.log(user, "Processing order");
            resp.sendRedirect(req.getContextPath() + "/warehouse/order/detail?id=" + orderId);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
