package com.example.controller.order;

import com.example.model.Order;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/warehouse/orders")
public class OrderQueueServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Order> orders = orderService.getSubmittedOrders();
        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/WEB-INF/warehouse/queue.jsp")
                .forward(req, resp);
    }
}

