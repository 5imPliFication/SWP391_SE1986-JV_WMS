package com.example.controller.inventory;

import com.example.dto.ProductItemDTO;
import com.example.dto.PurchaseRequestDTO;
import com.example.model.PurchaseRequestItem;
import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.InventoryService;
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

@WebServlet("/inventory/import")
@MultipartConfig
public class ImportProductItemServlet extends HttpServlet {

    private InventoryService inventoryService;
    private PurchaseRequestService purchaseRequestService;
    private ActivityLogService activityLogService;

    @Override
    public void init() throws ServletException {
        inventoryService = new InventoryService();
        purchaseRequestService = new PurchaseRequestService();
        activityLogService = new ActivityLogService();
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
        } else if ("file".equals(action)) {
            handleFile(session, request, response);
        } else if ("delete".equals(action)) {
            handleDelete(session, request, response);
        }
    }

    private void handleDelete(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");
        if (importItems == null) {
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        }

        // read arrays directly from form submission to preserve input data before
        // deleting
        String[] productIds = request.getParameterValues("productId");
        String[] serials = request.getParameterValues("serial");
        String[] prices = request.getParameterValues("price");

        if (productIds != null && serials != null && prices != null) {
            int maxLimit = Math.min(importItems.size(), productIds.length);

            for (int i = 0; i < maxLimit; i++) {
                ProductItemDTO dto = importItems.get(i);

                dto.setSerial(serials[i]);

                try {
                    dto.setImportPrice(Double.parseDouble(prices[i]));
                } catch (NumberFormatException e) {
                }
            }
        }

        // delete item by delete index
        String deleteIndexStr = request.getParameter("delete");
        int deleteIndex = Integer.parseInt(deleteIndexStr);
        importItems.remove(deleteIndex);

        session.setAttribute("importItems", importItems);
        response.sendRedirect(request.getContextPath() + "/inventory/import");
    }

    private void handleFile(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // get file excel import
        Part filePart = request.getPart("excelFile");

        // call to service to handle data from Excel
        List<ProductItemDTO> importProductItemDTOs = inventoryService.readProductItemsFromExcel(filePart);
        // set session
        session.setAttribute("importItems", importProductItemDTOs);
        response.sendRedirect(request.getContextPath() + "/inventory/import");
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
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        }

        // read arrays directly from form submission
        String[] productIds = request.getParameterValues("productId");
        String[] serials = request.getParameterValues("serial");
        String[] prices = request.getParameterValues("price");

        if (productIds == null || serials == null || prices == null || productIds.length == 0) {
            session.setAttribute("message", "No valid item data submitted.");
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        }

        // call service to handle import
        String resultSave = inventoryService.importProductItems(purchaseId, user.getId(), productIds, serials, prices);
        // message
        if (resultSave == null) {
            session.setAttribute("message", "Product item saved successfully");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", resultSave);
            session.setAttribute("messageType", "danger");
        }

        // delete session after save success
        session.removeAttribute("importItems");
        response.sendRedirect(request.getContextPath() + "/inventory/import-history");
    }

}
