package com.example.controller.order;

import com.example.model.*;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/salesman/order/detail")
public class SalesmanOrderDetailServlet extends HttpServlet {

    private OrderService orderService;
    private OrderItemService orderItemService;
    private ProductService productService;

    @Override
    public void init() {
        orderService = new OrderService();
        orderItemService = new OrderItemService();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("id"));

            int itemPage = 1;
            String itemPageRaw = req.getParameter("itemPage");
            if (itemPageRaw != null && !itemPageRaw.isBlank()) {
                try {
                    itemPage = Math.max(Integer.parseInt(itemPageRaw), 1);
                } catch (NumberFormatException ignored) {
                    itemPage = 1;
                }
            }

            int productPage = 1;
            String productPageRaw = req.getParameter("productPage");
            if (productPageRaw != null && !productPageRaw.isBlank()) {
                try {
                    productPage = Math.max(Integer.parseInt(productPageRaw), 1);
                } catch (NumberFormatException ignored) {
                    productPage = 1;
                }
            }

            int itemPageSize = 8;
            int productPageSize = 9;

            Order order = orderService.getOrderDetail(orderId, user.getId(), "Salesman");

            int totalItemCount = orderItemService.countItemsByOrder(orderId, user.getId(), "Salesman");
            int totalItemPages = (int) Math.ceil((double) totalItemCount / itemPageSize);
            if (totalItemPages == 0) totalItemPages = 1;
            if (itemPage > totalItemPages) itemPage = totalItemPages;
            int itemOffset = (itemPage - 1) * itemPageSize;
            List<OrderItem> items = orderItemService.getItemsByOrder(orderId, user.getId(), "Salesman", itemOffset, itemPageSize);

            int totalProductCount = productService.countAvailableProductsForOrder();
            int totalProductPages = (int) Math.ceil((double) totalProductCount / productPageSize);
            if (totalProductPages == 0) totalProductPages = 1;
            if (productPage > totalProductPages) productPage = totalProductPages;
            List<Product> availableProducts = productService.findAvailableProductsForOrder(productPage, productPageSize);

            req.setAttribute("order", order);
            req.setAttribute("items", items);
            req.setAttribute("availableProducts", availableProducts);
            req.setAttribute("itemPage", itemPage);
            req.setAttribute("itemPageSize", itemPageSize);
            req.setAttribute("totalItemPages", totalItemPages);
            req.setAttribute("totalItemCount", totalItemCount);
            req.setAttribute("productPage", productPage);
            req.setAttribute("productPageSize", productPageSize);
            req.setAttribute("totalProductPages", totalProductPages);
            req.setAttribute("totalProductCount", totalProductCount);

            req.getRequestDispatcher("/WEB-INF/salesman/order-detail.jsp")
                    .forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load order: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}



