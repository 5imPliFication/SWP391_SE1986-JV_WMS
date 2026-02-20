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
import java.util.List;

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
        String[] productNames = req.getParameterValues("productName[]");
        String[] brandNames = req.getParameterValues("brandId[]");
        String[] categoryNames = req.getParameterValues("categoryId[]");
        String[] quantities = req.getParameterValues("quantity[]");

        List<PurchaseRequestItem> items = new ArrayList<>();

        for (int i = 0; i < quantities.length; i++) {

            PurchaseRequestItem item = new PurchaseRequestItem();

            // 👉 Có chọn product có sẵn
            if (productIds[i] != null && !productIds[i].isBlank()) {
                item.setProductId(Long.valueOf(productIds[i]));
            } // 👉 New product
            else {
                item.setProductName(productNames[i]);
                item.setBrandName(brandNames[i]);
                item.setCategoryName(categoryNames[i]);
            }

            item.setQuantity(Long.valueOf(quantities[i]));
            items.add(item);
        }

        pr.updatePurchaseRequest(requestId, note, items, user);

        resp.sendRedirect(req.getContextPath()
                + "/purchase-request/list");
    }

}
