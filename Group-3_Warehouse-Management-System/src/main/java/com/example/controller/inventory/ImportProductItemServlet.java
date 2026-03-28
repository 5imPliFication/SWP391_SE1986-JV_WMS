package com.example.controller.inventory;

import com.example.dto.ProductItemDTO;
import com.example.dto.PurchaseRequestDTO;
import com.example.model.PurchaseRequestItem;
import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.InventoryService;
import com.example.service.ProductService;
import com.example.service.PurchaseRequestService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/import")
@MultipartConfig
public class ImportProductItemServlet extends HttpServlet {

    private InventoryService inventoryService;
    private PurchaseRequestService purchaseRequestService;
    private ActivityLogService activityLogService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        inventoryService = new InventoryService();
        purchaseRequestService = new PurchaseRequestService();
        activityLogService = new ActivityLogService();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get action
        String action = request.getParameter("action");

        // get session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            if ("import".equals(action)) {
                handleImport(session, request);
                activityLogService.log(user, "Import product");
            }
        } catch (Exception e) {
            session.setAttribute("message", "Cannot open import page: " + e.getMessage());
            session.setAttribute("messageType", "danger");
        }

        // get full list from session
        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");

        request.setAttribute("importItems", importItems);

        request.getRequestDispatcher("/WEB-INF/inventory/import-products.jsp").forward(request, response);
    }

    // handle import product
    private void handleImport(HttpSession session, HttpServletRequest request) {

        User user = (User) session.getAttribute("user");

        // get purchaseId from purchase request detail
        String purchaseIdStr = request.getParameter("purchaseId");

        // parse into long
        Long purchaseId = Long.parseLong(purchaseIdStr);

        // get purchase request
        PurchaseRequestDTO purchaseRequestDTO = purchaseRequestService.findPurchaseRequestById(purchaseId);
        if (purchaseRequestDTO == null) {
            throw new IllegalStateException("Purchase request not found (id=" + purchaseId + ")");
        }

        // set data for information of purchase request in jsp
        session.setAttribute("purchaseId", purchaseId);
        session.setAttribute("purchaseCode", purchaseRequestDTO.getPurchaseCode());
        session.setAttribute("purchaseNote", purchaseRequestDTO.getNote());
        session.setAttribute("createdBy", purchaseRequestDTO.getCreatedBy());
        session.setAttribute("createdAt", purchaseRequestDTO.getCreatedAt());
        session.setAttribute("approvedBy", purchaseRequestDTO.getApprovedBy());
        session.setAttribute("approvedAt", purchaseRequestDTO.getApprovedAt());
        session.setAttribute("handleBy", user.getFullName());

        // get purchase request items
        List<PurchaseRequestItem> purchaseRequestItems = purchaseRequestService.getItems(purchaseId);

        List<ProductItemDTO> importItems = new ArrayList<>();
        // convert purchase request item -> product item
        for (PurchaseRequestItem purchaseRequestItem : purchaseRequestItems) {
            for (int i = 0; i < purchaseRequestItem.getQuantity(); i++) {
                ProductItemDTO dto = new ProductItemDTO();
                dto.setProductId(purchaseRequestItem.getProductId());
                dto.setProductName(purchaseRequestItem.getProductName());
                dto.setSerial("");
                dto.setImportPrice(null);
                dto.setUnit(purchaseRequestItem.getUnit());
                // add product item to list importItems
                importItems.add(dto);
            }
        }
        // save list items to session for pagination and delete
        session.setAttribute("importItems", importItems);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        if ("save".equals(action)) {
            handleSave(session, request, response);
        }
    }

    // save purchase request and others information relative
    private void handleSave(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // get purchaseId in form save
        Long purchaseId = Long.parseLong(request.getParameter("purchaseId"));

        // get information warehouse handle import purchase
        User user = (User) session.getAttribute("user");

        // get full list from session (all pages)
        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");

        if (importItems == null || importItems.isEmpty()) {
            session.setAttribute("message", "No product items to save");
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/import");
            return;
        }

        // read arrays directly from form submission
        String supplier = request.getParameter("supplier");
        String[] productIds = request.getParameterValues("productId");
        String[] serials = request.getParameterValues("serial");
        String[] prices = request.getParameterValues("price");

        if (productIds == null || serials == null || prices == null || productIds.length == 0) {
            session.setAttribute("message", "No valid item data submitted.");
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/import");
            return;
        }

        // call service to handle import
        String resultSave = inventoryService.importProductItems(purchaseId, user.getId(), productIds, serials, prices, supplier);

        // update current price for product in inventory after import success
        if (resultSave == null) {
            for (int i = 0; i < productIds.length; i++) {
                long productId = Long.parseLong(productIds[i]);
                double price = Double.parseDouble(prices[i]);
                productService.updateCurrentPriceByProductId(productId, price);
            }
        }

        // message
        if (resultSave == null) {
            session.setAttribute("message", "Product item saved successfully");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", resultSave);
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/import");
            return;
        }

        // delete session after save success
        session.removeAttribute("importItems");
        response.sendRedirect(request.getContextPath() + "/import-history");
    }

}
