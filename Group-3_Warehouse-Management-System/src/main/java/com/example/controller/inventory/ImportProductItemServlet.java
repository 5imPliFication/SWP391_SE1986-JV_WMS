package com.example.controller.inventory;

import com.example.dto.ProductItemDTO;
import com.example.dto.PurchaseRequestDTO;
import com.example.model.PurchaseRequestItem;
import com.example.model.User;
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

    @Override
    public void init() throws ServletException {
        inventoryService = new InventoryService();
        purchaseRequestService = new PurchaseRequestService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get action
        String action = request.getParameter("action");

        // get session
        HttpSession session = request.getSession();

        try {
            if ("delete".equals(action)) {
                handleDelete(session, request, response);
                return;
            } else if ("import".equals(action)) {
                handleImport(session, request);
            }
        } catch (Exception e) {
            session.setAttribute("message", "Cannot open import page: " + e.getMessage());
            session.setAttribute("messageType", "danger");
        }

        // get full list from session
        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");

        // pagination for display 
        if (importItems != null && !importItems.isEmpty()) {
            int pageSize = AppConstants.PAGE_SIZE;
            int totalItems = importItems.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);

            // get pageNo
            String pageNoStr = request.getParameter("pageNo");
            int pageNo = AppConstants.DEFAULT_PAGE_NO;
            if (pageNoStr != null && !pageNoStr.isEmpty()) {
                try {
                    pageNo = Integer.parseInt(pageNoStr);
                } catch (NumberFormatException e) {
                    pageNo = AppConstants.DEFAULT_PAGE_NO;
                }
            }


            int start = (pageNo - 1) * pageSize;
            int end = Math.min(start + pageSize, totalItems);

            List<ProductItemDTO> pageItems = importItems.subList(start, end);

            request.setAttribute("importItems", pageItems);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("startIndex", start);
        } else {
            request.setAttribute("importItems", importItems);
        }

        request.getRequestDispatcher("/WEB-INF/inventory/import-products.jsp").forward(request, response);
    }

    // handle import product
    private void handleImport(HttpSession session, HttpServletRequest request) {

        // get purchaseId from purchase request detail
        String purchaseIdRaw = request.getParameter("purchaseId");
        if (purchaseIdRaw == null || purchaseIdRaw.isBlank()) {
            throw new IllegalArgumentException("Missing purchaseId");
        }

        Long purchaseId;
        try {
            purchaseId = Long.parseLong(purchaseIdRaw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid purchaseId: " + purchaseIdRaw);
        }

        // get purchase request
        PurchaseRequestDTO purchaseRequestDTO = purchaseRequestService.findPurchaseRequestById(purchaseId);
        if (purchaseRequestDTO == null) {
            throw new IllegalStateException("Purchase request not found (id=" + purchaseId + ")");
        }

        // set data for information of purchase request in jsp
        session.setAttribute("purchaseId", purchaseId);
        session.setAttribute("purchaseCode", purchaseRequestDTO.getPurchaseCode());
        session.setAttribute("purchaseNote", purchaseRequestDTO.getNote());

        // get purchase request items
        List<PurchaseRequestItem> purchaseRequestItems = purchaseRequestService.getItems(purchaseId);
        if (purchaseRequestItems == null || purchaseRequestItems.isEmpty()) {
            throw new IllegalStateException("Purchase request has no items (id=" + purchaseId + ")");
        }

        List<ProductItemDTO> importItems = new ArrayList<>();
        // convert purchase request item -> product item
        for (PurchaseRequestItem purchaseRequestItem : purchaseRequestItems) {
            if (purchaseRequestItem.getQuantity() == null || purchaseRequestItem.getQuantity() <= 0) {
                continue;
            }
            if (purchaseRequestItem.getProductId() == null) {
                continue;
            }
            for (int i = 0; i < purchaseRequestItem.getQuantity(); i++) {
                ProductItemDTO dto = new ProductItemDTO();
                dto.setProductId(purchaseRequestItem.getProductId());
                dto.setProductName(purchaseRequestItem.getProductName());
                dto.setSerial("");
                dto.setImportPrice(null);
                // add product item to list importItems
                importItems.add(dto);
            }
        }
        if (importItems.isEmpty()) {
            throw new IllegalStateException("No importable items for purchase request (id=" + purchaseId + ")");
        }
        // save list items to session for pagination and delete
        session.setAttribute("importItems", importItems);
    }

    // delete 1 product items from list import product items
    private void handleDelete(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // get list importItems from session
        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");

        // get index need to delete
        if (importItems != null) {
            int index = Integer.parseInt(request.getParameter("index"));
            importItems.remove(index);
        }
        session.setAttribute("importItems", importItems);
        response.sendRedirect(request.getContextPath() + "/inventory/import");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String pagingParam = request.getParameter("paging");
        String action = request.getParameter("action");

        if (pagingParam != null) {
            handlePaging(session, request, response);
        } else if ("save".equals(action)) {
            handleSave(session, request, response);
        } else if ("file".equals(action)) {
            handleFile(session, request, response);
        }
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
        String purchaseIdRaw = request.getParameter("purchaseId");
        if (purchaseIdRaw == null || purchaseIdRaw.isBlank()) {
            session.setAttribute("message", "Missing purchaseId");
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        }

        Long purchaseId;
        try {
            purchaseId = Long.parseLong(purchaseIdRaw);
        } catch (NumberFormatException e) {
            session.setAttribute("message", "Invalid purchaseId: " + purchaseIdRaw);
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        }

        // get information warehouse handle import purchase
        User user = (User) session.getAttribute("user");
        if (user == null) {
            session.setAttribute("message", "Unauthorized");
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        }

        // get full list from session (all pages)
        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");

        if (importItems == null || importItems.isEmpty()) {
            session.setAttribute("message", "No product items to save");
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        }

        // update data in current page into session before save
        String[] indexesStr = request.getParameterValues("rowIndex");
        String[] serialsStr = request.getParameterValues("serial");
        String[] pricesStr = request.getParameterValues("price");

        if (indexesStr != null && serialsStr != null && pricesStr != null) {
            for (int i = 0; i < indexesStr.length; i++) {
                // index of row
                int index = Integer.parseInt(indexesStr[i]);
                if (index >= 0 && index < importItems.size()) {
                    ProductItemDTO dto = importItems.get(index);
                    dto.setSerial(serialsStr[i]);
                    dto.setImportPrice(Long.parseLong(pricesStr[i]));
                }
            }
        }

        String[] productIds = new String[importItems.size()];
        String[] serials = new String[importItems.size()];
        String[] prices = new String[importItems.size()];

        for (int i = 0; i < importItems.size(); i++) {
            ProductItemDTO dto = importItems.get(i);
            productIds[i] = dto.getProductId() != null ? String.valueOf(dto.getProductId()) : null;
            serials[i] = dto.getSerial();
            prices[i] = dto.getImportPrice() != null ? String.valueOf(dto.getImportPrice()) : null;
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
        response.sendRedirect(request.getContextPath() + "/inventory/import");
    }

    // update importItems in session when changing page
    private void handlePaging(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");

        // index
        String[] indexes = request.getParameterValues("rowIndex");
        String[] serials = request.getParameterValues("serial");
        String[] prices = request.getParameterValues("price");

        if (indexes != null && serials != null && prices != null) {
            for (int i = 0; i < indexes.length; i++) {
                // index of row
                int index = Integer.parseInt(indexes[i]);
                if (index >= 0 && index < importItems.size()) {
                    ProductItemDTO dto = importItems.get(index);
                    dto.setSerial(serials[i]);
                    dto.setImportPrice(Long.parseLong(prices[i]));
                }
            }
        }

        session.setAttribute("importItems", importItems);

        // redirect to page click on pagination
        String pageNo = request.getParameter("paging");
        if (pageNo == null || pageNo.isEmpty()) {
            pageNo = "1";
        }
        response.sendRedirect(request.getContextPath() + "/inventory/import?pageNo=" + pageNo);
    }
}
