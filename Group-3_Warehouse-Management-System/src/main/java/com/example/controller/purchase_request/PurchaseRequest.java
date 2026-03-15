package com.example.controller.purchase_request;

import com.example.model.PurchaseRequestItem;
import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.ChipService;
import com.example.service.ModelService;
import com.example.service.PurchaseRequestService;
import com.example.service.RamService;
import com.example.service.SizeService;
import com.example.service.StorageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/purchase-request/create")
public class PurchaseRequest extends HttpServlet {

    private PurchaseRequestService p;
    private ModelService m;
    private ChipService c;
    private RamService r;
    private StorageService sto;
    private SizeService s;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        p = new PurchaseRequestService();
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

        request.setAttribute("productName", p.getProductDropdown());
        request.setAttribute("brands", p.getBrandsDropdown());
        request.setAttribute("categories", p.getCategoryDropdown());
        request.setAttribute("models", m.getAllActiveModels());
        request.setAttribute("chips", c.getAllActiveChips());
        request.setAttribute("rams", r.getAllActiveRams());
        request.setAttribute("storages", sto.getAllActiveStorage());
        request.setAttribute("sizes", s.getAllActiveSize());

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
            String[] quantities = request.getParameterValues("quantity[]");

            if (quantities == null || quantities.length == 0) {
                throw new RuntimeException("Purchase request must have at least one item");
            }

            Map<Long, Long> productQuantityMap = new LinkedHashMap<>();

            for (int i = 0; i < quantities.length; i++) {
                Long productId = Long.parseLong(productIds[i]);
                Long quantity = Long.parseLong(quantities[i]);

                productQuantityMap.put(
                        productId,
                        productQuantityMap.getOrDefault(productId, 0L) + quantity
                );
            }

            List<PurchaseRequestItem> items = new ArrayList<>();

            for (Map.Entry<Long, Long> entry : productQuantityMap.entrySet()) {
                PurchaseRequestItem item = new PurchaseRequestItem();
                item.setProductId(entry.getKey());
                item.setQuantity(entry.getValue());
                items.add(item);
            }

            p.createPurchaseRequest(user.getId(), note, items);
            activityLogService.log(user, "Create purchase request");
            response.sendRedirect(request.getContextPath() + "/purchase-request/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/purchase_request/CreatePurchaseRequest.jsp")
                    .forward(request, response);
        }
    }

}
