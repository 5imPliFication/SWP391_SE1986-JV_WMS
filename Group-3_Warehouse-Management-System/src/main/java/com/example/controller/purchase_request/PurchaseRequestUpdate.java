/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.purchase_request;

import com.example.model.PurchaseRequestItem;
import com.example.model.User;
import com.example.service.PurchaseRequestService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC
 */
@WebServlet(name = "PurchaseRequestUpdate", urlPatterns = {"/purchase-request/edit"})
public class PurchaseRequestUpdate extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PurchaseRequestUpdate</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PurchaseRequestUpdate at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private PurchaseRequestService pr;

    @Override
    public void init() {
        pr = new PurchaseRequestService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long requestId = Long.valueOf(req.getParameter("id"));
        String note = req.getParameter("note");

        String[] productIds = req.getParameterValues("productId[]");
        String[] quantities = req.getParameterValues("quantity[]");

        // 1. Sử dụng Map để gộp các sản phẩm trùng ID
        Map<Long, Long> aggregatedItems = new LinkedHashMap<>(); 

        if (productIds != null && quantities != null) {
            for (int i = 0; i < productIds.length; i++) {
                Long pId = Long.valueOf(productIds[i]);
                Long qty = Long.valueOf(quantities[i]);

                // Nếu đã có ID này rồi thì cộng thêm số lượng, nếu chưa thì tạo mới (mặc định 0L)
                aggregatedItems.put(pId, aggregatedItems.getOrDefault(pId, 0L) + qty);
            }
        }

        // 2. Chuyển từ Map ngược lại List để truyền vào hàm update
        List<PurchaseRequestItem> items = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : aggregatedItems.entrySet()) {
            PurchaseRequestItem item = new PurchaseRequestItem();
            item.setProductId(entry.getKey());
            item.setQuantity(entry.getValue());
            items.add(item);
        }

        pr.updatePurchaseRequest(requestId, note, items, user);

        resp.sendRedirect(req.getContextPath() + "/purchase-request/list");
    }

}
