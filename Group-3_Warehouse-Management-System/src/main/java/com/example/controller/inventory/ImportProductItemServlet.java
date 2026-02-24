package com.example.controller.inventory;

import com.example.dto.ImportProductItemDTO;
import com.example.model.Product;
import com.example.service.InventoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/inventory/import")
@MultipartConfig
public class ImportProductItemServlet extends HttpServlet {

    private InventoryService inventoryService;

    @Override
    public void init() throws ServletException {
        inventoryService = new InventoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get action
        String action = request.getParameter("action");

        // get session
        HttpSession session = request.getSession();

        if ("search".equals(action)) {
            String searchName = request.getParameter("searchName");
            // set search name product for session
            session.setAttribute("searchName", searchName);
            // call service to get list products
            List<Product> products = inventoryService.findProductByName(searchName);
            if (products == null || products.isEmpty()) {
                request.setAttribute("error", "Product not found. Please add new product");
            } else {
                request.setAttribute("products", products);
            }
        } else if ("delete".equals(action)) {
            // get session
            List<ImportProductItemDTO> importItems = (List<ImportProductItemDTO>) session.getAttribute("IMPORT_ITEMS");

            // get index need to delete
            if (importItems != null) {
                int index = Integer.parseInt(request.getParameter("index"));
                importItems.remove(index);
            }
            session.setAttribute("IMPORT_ITEMS", importItems);
            response.sendRedirect(request.getContextPath() + "/inventory/import");
            return;
        } else {
            // view or refresh
            String searchName = (String) session.getAttribute("searchName");
            // if exist -> view again
            if (searchName != null) {
                List<Product> products = inventoryService.findProductByName(searchName);
                request.setAttribute("products", products);
            }
        }

        // pagination for IMPORT_ITEMS
        List<ImportProductItemDTO> importItems = (List<ImportProductItemDTO>) session.getAttribute("IMPORT_ITEMS");
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

            // index start cut in IMPORT_ITEMS
            int start = (pageNo - 1) * pageSize;
            // index end cut in IMPORT_ITEMS
            int end = Math.min(start + pageSize, totalItems);

            // cut
            List<ImportProductItemDTO> pageItems = importItems.subList(start, end);

            request.setAttribute("importItems", pageItems);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageNo", pageNo);
        }

        request.getRequestDispatcher("/WEB-INF/inventory/import-products.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get action
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            // get session
            HttpSession session = request.getSession();
            List<ImportProductItemDTO> importItems = (List<ImportProductItemDTO>) session.getAttribute("IMPORT_ITEMS");

            // if dont have value -> init
            if (importItems == null) {
                importItems = new ArrayList<>();
            }

            // get information from form submit with action has value add
            Long productId = Long.parseLong(request.getParameter("product-id"));
            String productName = request.getParameter("product-name");

            // init import product item dto
            ImportProductItemDTO importProductItemDTO = new ImportProductItemDTO();
            importProductItemDTO.setProductId(productId);
            importProductItemDTO.setProductName(productName);
            importProductItemDTO.setSerial("");
            importProductItemDTO.setImportPrice(0.0);

            importItems.add(importProductItemDTO);

            // save in session
            session.setAttribute("IMPORT_ITEMS", importItems);
            response.sendRedirect(request.getContextPath() + "/inventory/import");
        } else if ("save".equals(action)) {
            // get session
            HttpSession session = request.getSession();

            // get value
            String[] serials = request.getParameterValues("serial");
            String[] prices = request.getParameterValues("price");
            String[] productIds = request.getParameterValues("product-id");

            // message
            if (inventoryService.saveProductItems(serials, prices, productIds) == null) {
                session.setAttribute("message", "Product item saved successfully");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", inventoryService.saveProductItems(serials, prices, productIds));
                session.setAttribute("messageType", "danger");
            }

            // delete session after save success
            session.removeAttribute("IMPORT_ITEMS");
            response.sendRedirect(request.getContextPath() + "/inventory/import");
        } else if ("file".equals(action)) {
            HttpSession session = request.getSession();
            // get file excel import
            Part filePart = request.getPart("excelFile");

            // call to service to handle data from Excel
            List<ImportProductItemDTO> importProductItemDTOs = inventoryService.readProductItemsFromExcel(filePart);
            // set session
            session.setAttribute("IMPORT_ITEMS", importProductItemDTOs);
            response.sendRedirect(request.getContextPath() + "/inventory/import");
        }
    }
}
