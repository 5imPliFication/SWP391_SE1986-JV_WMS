package com.example.controller.inventory;

import com.example.dao.OrderDAO;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/inventory/update-status")
public class UpdateStatusServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        // get data
        String orderCode = request.getParameter("orderCode");
        String newStatus = request.getParameter("newStatus");

        // call service
        boolean statusUpdateOrder = orderService.updateStatusOrder(orderCode, newStatus);

        if(statusUpdateOrder){
            session.setAttribute("message", "Order Updated Successfully");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Order Update Failed");
            session.setAttribute("messageType", "danger");
        }
        response.sendRedirect(request.getContextPath() + "/inventory/export");
    }
}