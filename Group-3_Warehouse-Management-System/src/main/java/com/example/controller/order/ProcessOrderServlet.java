package com.example.controller.order;

import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/warehouse/order/process")
public class ProcessOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("user");
        Long orderId = Long.parseLong(req.getParameter("orderId"));

        orderService.startProcessing(orderId, user.getId());

        resp.sendRedirect(req.getContextPath()
                + "/warehouse/orders");
    }
}
