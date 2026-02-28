package com.example.controller.inventory;

import com.example.dto.ProductItemDTO;
import com.example.dto.PurchaseRequestDTO;
import com.example.model.PurchaseRequestItem;
import com.example.model.User;
import com.example.service.InventoryService;
import com.example.service.PurchaseRequestService;
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

        if ("delete".equals(action)) {
            handleDelete(session, request, response);
            return;
        } else if ("import".equals(action)) {
            handleImport(session, request);
        }

        // pagination for importItems
        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");
        if (importItems != null && !importItems.isEmpty()) {
            int pageSize = 10;
            int totalItems = importItems.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);

            // get pageNo
            String pageNoStr = request.getParameter("pageNo");
            // default value
            int pageNo = 1;
            if (pageNoStr != null && !pageNoStr.isEmpty()) {
                try {
                    pageNo = Integer.parseInt(pageNoStr);
                } catch (NumberFormatException e) {
                    pageNo = 1;
                }
            }

            // validate pageNo in range
            if (pageNo < 1)
                pageNo = 1;
            if (pageNo > totalPages)
                pageNo = totalPages;

            // index start cut in session importItems
            int start = (pageNo - 1) * pageSize;
            // index end cut in session importItems
            int end = Math.min(start + pageSize, totalItems);

            // cut
            List<ProductItemDTO> pageItems = importItems.subList(start, end);

            request.setAttribute("importItems", pageItems);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageNo", pageNo);
        }

        request.getRequestDispatcher("/WEB-INF/inventory/import-products.jsp").forward(request, response);
    }

    private void handleImport(HttpSession session, HttpServletRequest request) {

        // get purchaseId from purchase request detail
        Long purchaseId = Long.parseLong(request.getParameter("purchaseId"));

        // get purchase request
        PurchaseRequestDTO purchaseRequestDTO = purchaseRequestService.findPurchaseById(purchaseId);

        // set data for jsp
        request.setAttribute("purchaseId", purchaseId);
        request.setAttribute("purchaseCode", purchaseRequestDTO.getPurchaseCode());
        request.setAttribute("purchaseNote", purchaseRequestDTO.getNote());

        // get list purchase request items
        List<PurchaseRequestItem> purchaseRequestItems = purchaseRequestService.getItems(purchaseId);

        List<ProductItemDTO> importItems = new ArrayList<>();
        for (PurchaseRequestItem purchaseRequestItem : purchaseRequestItems) {
            for (int i = 0; i < purchaseRequestItem.getQuantity(); i++) {
                ProductItemDTO dto = new ProductItemDTO();
                dto.setProductId(purchaseRequestItem.getProductId());
                dto.setProductName(purchaseRequestItem.getProductName());
                dto.setSerial("");
                dto.setImportPrice(0.0);
                importItems.add(dto);
            }
        }
        // save list items to session for pagination and delete
        session.setAttribute("importItems", importItems);
    }

    // delete 1 product items from list import product items
    private void handleDelete(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
        // get action
        String action = request.getParameter("action");
        if ("save".equals(action)) {
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

    private void handleSave(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // get purchaseId in form save
        Long purchaseId = Long.parseLong(request.getParameter("purchaseId"));

        // get note
        String note = request.getParameter("note");

        // get warehouse login
        User user = (User) session.getAttribute("user");

        // get value
        String[] productIds = request.getParameterValues("productId");
        String[] serials = request.getParameterValues("serial");
        String[] prices = request.getParameterValues("price");

        // call service to handle import
        String resultSave = inventoryService.importProductItems(purchaseId, user.getId(), note, productIds, serials, prices);
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
        session.removeAttribute("message");
        session.removeAttribute("messageType");
        response.sendRedirect(request.getContextPath() + "/inventory/import");
    }
}
