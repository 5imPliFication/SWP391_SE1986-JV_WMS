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
import com.example.service.ActivityLogService;
import com.example.service.ChipService;
import com.example.service.ModelService;
import com.example.service.RamService;
import com.example.service.SizeService;
import com.example.service.StorageService;

/**
 *
 * @author PC
 */
@WebServlet(name = "PurchaseRequestDetail", urlPatterns = {"/purchase-request/detail"})
public class PurchaseRequestDetail extends HttpServlet {

    private PurchaseRequestService pr;
    private ModelService m;
    private ChipService c;
    private RamService r;
    private StorageService sto;
    private SizeService s;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        pr = new PurchaseRequestService();
        m = new ModelService();
        c = new ChipService();
        r = new RamService();
        sto = new StorageService();
        s = new SizeService();
        activityLogService = new ActivityLogService();
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
        request.setAttribute("brands", pr.getBrandsDropdown());
        request.setAttribute("categories", pr.getCategoryDropdown());
        request.setAttribute("models", m.getAllActiveModels());
        request.setAttribute("chips", c.getAllActiveChips());
        request.setAttribute("rams", r.getAllActiveRams());
        request.setAttribute("storages", sto.getAllActiveStorage());
        request.setAttribute("sizes", s.getAllActiveSize());

        request.getRequestDispatcher("/WEB-INF/purchase_request/PurchaseRequestDetail.jsp").forward(request, response);
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
            case "cancel" -> {
                pr.cancel(id);
                activityLogService.log(user, "Cancel purchase request");
            }
            case "approve" -> {
                pr.approve(id, user.getId());
                activityLogService.log(user, "Approve purchase request");
            }
            case "reject" -> {
                pr.reject(id, user.getId());
                activityLogService.log(user, "Reject purchase request");
            }
            case "complete" -> {
                pr.complete(id);
                activityLogService.log(user, "Complete purchase request");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/purchase-request/list");
    }
}
