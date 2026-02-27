package com.example.controller.order;

import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/salesman/order/item/add")
public class AddOrderItemServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            Long productItemId = Long.parseLong(req.getParameter("productItemId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            // Check if item already exists and update quantity instead
            orderService.addOrUpdateOrderItem(orderId, productItemId, quantity);

            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to add item: " + e.getMessage());
        }
    }
}