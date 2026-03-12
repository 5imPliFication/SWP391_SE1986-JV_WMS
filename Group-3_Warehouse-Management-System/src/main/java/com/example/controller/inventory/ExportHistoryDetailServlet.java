package com.example.controller.inventory;

import com.example.dao.OrderItemDAO;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/inventory/export-history/detail")
public class ExportHistoryDetailServlet extends HttpServlet {
    private OrderService orderService;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        orderItemDAO = new OrderItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/inventory/export-history");
            return;
        }

        try {
            Long orderId = Long.parseLong(idStr);
            
            // Allow any user role to view the history, or check session user role
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            User user = (User) session.getAttribute("user");
            
            // Get order detail
            Order order = orderService.getOrderDetail(orderId, user.getId(), user.getRole().getName());
            
            if (order == null || !"COMPLETED".equals(order.getStatus())) {
                request.setAttribute("error", "Order not found or not in completed status.");
                request.getRequestDispatcher("/WEB-INF/inventory/export-history.jsp").forward(request, response);
                return;
            }

            // Get order items and their products mapping
            List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
            
            request.setAttribute("order", order);
            request.setAttribute("items", items);
            
            request.getRequestDispatcher("/WEB-INF/inventory/export-history-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/inventory/export-history");
        } catch (SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
