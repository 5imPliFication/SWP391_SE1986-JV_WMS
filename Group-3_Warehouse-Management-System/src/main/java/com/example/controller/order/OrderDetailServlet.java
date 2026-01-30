package com.example.controller.order;

import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.User;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/salesman/order/detail")
public class OrderDetailServlet extends HttpServlet {

    private OrderService orderService;
    private OrderItemService orderItemService;

    @Override
    public void init() {
        orderService = new OrderService();
        orderItemService = new OrderItemService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        Long orderId = Long.parseLong(req.getParameter("id"));

        Order order = orderService.getOrderDetail(orderId, user.getId(), user.getRole().getName());
        List<OrderItem> items =
                orderItemService.getItemsByOrder(orderId, user.getId(), user.getRole().getName());

        req.setAttribute("order", order);
        req.setAttribute("items", items);

        req.getRequestDispatcher("/WEB-INF/salesman/order-detail.jsp")
                .forward(req, resp);
    }
}



