package com.example.controller.order;

import com.example.model.Order;
import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import  jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/salesman/orders")
public class MyOrdersServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");

        List<Order> orders = orderService.getOrdersBySalesman(user.getId());
        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/WEB-INF/salesman/orders.jsp")
                .forward(req, resp);
    }
}
