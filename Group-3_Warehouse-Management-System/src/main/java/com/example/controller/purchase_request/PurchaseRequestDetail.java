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

        String idRaw = request.getParameter("id");
        if (idRaw == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long id;
        try {
            id = Long.parseLong(idRaw);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        PurchaseRequest prDetail = pr.getDetail(id, user);
        if (prDetail == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        List<PurchaseRequestItem> items = pr.getItems(id);

        request.setAttribute("prList", prDetail);
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
        String action = req.getParameter("action");

        switch (action) {
            case "cancel" ->
                pr.cancel(id);
            case "approve" ->
                pr.approve(id);
            case "reject" ->
                pr.reject(id);
        }

        resp.sendRedirect(req.getContextPath() + "/purchase-request/list");
    }
}
