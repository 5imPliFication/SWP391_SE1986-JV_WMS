package com.example.controller.inventory;

import com.example.dto.ProductItemDTO;
import com.example.dto.ProductDTO;
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
            handleSearch(session, request);
        } else if ("delete".equals(action)) {
            handleDelete(session, request, response);
            return;
        }

        // Handle Purchase Request Code if redirected from detail page
        String prCode = request.getParameter("prCode");
        if (prCode != null && !prCode.isEmpty()) {
            request.setAttribute("prCode", prCode);
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

    // search products by name
    private void handleSearch(HttpSession session, HttpServletRequest request) {
        String name = request.getParameter("name");

        // set search name product for session
        session.setAttribute("name", name);

        // call service to get list products
        List<ProductDTO> products = inventoryService.searchProducts(name);
        if (products == null || products.isEmpty()) {
            request.setAttribute("error", "Product not found. Please add new product");
        } else {
            session.setAttribute("products", products);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // get action
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            handleAdd(session, request, response);
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

    private void handleSave(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // get value
        String[] productIds = request.getParameterValues("productId");
        String[] serials = request.getParameterValues("serial");
        String[] prices = request.getParameterValues("price");

        // call service to handle save
        String resultSave = inventoryService.saveProductItems(productIds, serials, prices);
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

    private void handleAdd(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductItemDTO> importItems = (List<ProductItemDTO>) session.getAttribute("importItems");

        // get session
        List<ProductDTO> products = (List<ProductDTO>) session.getAttribute("products");
        if (products != null) {
            products = inventoryService.searchProducts((String) session.getAttribute("name"));
            session.setAttribute("products", products);
        }

        // if session haven't value -> init
        if (importItems == null) {
            importItems = new ArrayList<>();
        }

        // get information from list product after search
        Long productId = Long.parseLong(request.getParameter("productId"));
        String productName = request.getParameter("productName");

        // init import product item dto
        ProductItemDTO importProductItemDTO = new ProductItemDTO();
        importProductItemDTO.setProductId(productId);
        importProductItemDTO.setProductName(productName);
        importProductItemDTO.setSerial("");
        importProductItemDTO.setImportPrice(0.0);

        importItems.add(importProductItemDTO);

        // save in session
        session.setAttribute("importItems", importItems);
        response.sendRedirect(request.getContextPath() + "/inventory/import");
    }
}
