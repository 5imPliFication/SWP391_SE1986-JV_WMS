package com.example.controller.purchase_request;

import com.example.model.PurchaseRequestItem;
import com.example.model.User;
import com.example.service.PurchaseRequestService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/purchase-request/create")
public class PurchaseRequest extends HttpServlet {

    private PurchaseRequestService p;

    @Override
    public void init() {
        p = new PurchaseRequestService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("productName", p.getProductDropdown());
        request.setAttribute("BrandName", p.getBrandsDropdown());
        request.setAttribute("CategoryName", p.getCategoryDropdown());

        request.getRequestDispatcher("/WEB-INF/purchase_request/CreatePurchaseRequest.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            User user = (User) request.getSession().getAttribute("user");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String note = request.getParameter("note");

            String[] productIds = request.getParameterValues("productId[]");
            String[] productNames = request.getParameterValues("productName[]");
            String[] brandNames = request.getParameterValues("brandId[]");
            String[] categoryNames = request.getParameterValues("categoryId[]");
            String[] quantities = request.getParameterValues("quantity[]");

            if (quantities == null || quantities.length == 0) {
                throw new RuntimeException("Purchase request must have at least one item");
            }

            List<PurchaseRequestItem> items = new ArrayList<>();

            for (int i = 0; i < quantities.length; i++) {
                PurchaseRequestItem item = new PurchaseRequestItem();

                String productIdStr = productIds[i];

                if (productIdStr != null && !productIdStr.trim().isEmpty()) {
                    // sản phẩm đã tồn tại
                    item.setProductId(Long.parseLong(productIdStr));
                } else {
                    // sản phẩm mới (proposal)
                    item.setProductName(productNames[i]);
                    item.setBrandName(brandNames[i]);
                    item.setCategoryName(categoryNames[i]);
                }

                item.setQuantity(Long.parseLong(quantities[i]));
                items.add(item);
            }

            p.createPurchaseRequest(user.getId(), note, items);

            response.sendRedirect(request.getContextPath() + "/purchase-request/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/purchase_request/CreatePurchaseRequest.jsp")
                    .forward(request, response);
        }
    }

}
