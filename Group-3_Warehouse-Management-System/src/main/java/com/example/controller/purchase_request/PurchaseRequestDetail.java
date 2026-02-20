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
import java.util.List;
import com.example.model.PurchaseRequest;

/**
 *
 * @author PC
 */
@WebServlet(name = "PurchaseRequestDetail", urlPatterns = {"/purchase-request/detail"})
public class PurchaseRequestDetail extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PurchaseRequestDetail</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PurchaseRequestDetail at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));

        PurchaseRequestService service = new PurchaseRequestService();

        PurchaseRequest list = pr.getDetail(id, user);
        List<PurchaseRequestItem> items = service.getItems(id);

        request.setAttribute("prList", list);
        request.setAttribute("items", items);
        request.setAttribute("user", user);
        request.setAttribute("productName", pr.getProductDropdown());
        request.setAttribute("BrandName", pr.getBrandsDropdown());
        request.setAttribute("CategoryName", pr.getCategoryDropdown());

        request.getRequestDispatcher(
                "/WEB-INF/purchase_request/PurchaseRequestDetail.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long id = Long.valueOf(req.getParameter("id"));
        String action = req.getParameter("action"); // cancel | approve | reject

        switch (action) {
            case "cancel":
                pr.cancel(id);
                break;

            case "approve":
                pr.approve(id);
                break;

            case "reject":
                pr.reject(id);
                break;
        }

        resp.sendRedirect(req.getContextPath() + "/purchase-request/list");
    }

}
