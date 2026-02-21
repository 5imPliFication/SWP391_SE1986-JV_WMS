package com.example.controller.audit;

import com.example.dto.ImportProductItemDTO;
import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.InventoryAuditItem;
import com.example.model.Product;
import com.example.service.*;
import com.example.util.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/inventory-audits/add")
@MultipartConfig
public class InventoryAuditAddServlet extends HttpServlet {

    private InventoryAuditService inventoryAuditService;
    private ProductService productService;
    private BrandService brandService;
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        inventoryAuditService = new InventoryAuditService();
        productService = new ProductService();
        brandService = new BrandService();
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchName = request.getParameter("searchName");
        String brandName = request.getParameter("brandName");
        String categoryName = request.getParameter("categoryName");

        String isActiveParam = request.getParameter("isActive");
        // Nếu parse sang boolean ngay sẽ lỗi All Status == false (parse("") => false)
        Boolean isActive = null;
        if (isActiveParam != null && !isActiveParam.isEmpty()) {
            isActive = Boolean.valueOf(isActiveParam);
        }

        // Using when the first time load this page (No param pageNo in URL)
        int pageNo = 1;
        // Get total record
        int totalProducts = productService.getTotalProducts(searchName, brandName, categoryName, isActive);
        // Count total pages
        int totalPages = (int) Math.ceil((double) totalProducts / UserConstant.PAGE_SIZE);

        if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").isEmpty()) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        List<Product> products = productService.findAll(searchName, brandName, categoryName, isActive, pageNo);
        List<Brand> brands = brandService.getActiveBrands();
        List<Category> categories = categoryService.getActiveCategories();
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.setAttribute("products", products);
        request.setAttribute("totalPages", totalPages);
        // Use to determine active page number for the first time
        request.setAttribute("pageNo", pageNo);

        // Use for showing temp audit list before save to database
        HttpSession session = request.getSession();
        List<InventoryAuditItem> tmpInventoryAuditItems = (List<InventoryAuditItem>) session.getAttribute("TMP_INVENTORY_AUDIT_ITEMS");
        if (tmpInventoryAuditItems != null) {
            request.setAttribute("tmpInventoryAuditItems", tmpInventoryAuditItems);
        }
        request.getRequestDispatcher("/WEB-INF/audit/inventory-audit-add.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get action
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            // get session
            HttpSession session = request.getSession();
            List<InventoryAuditItem> tmpInventoryAuditItems = (List<InventoryAuditItem>) session.getAttribute("TMP_INVENTORY_AUDIT_ITEMS");

            // if dont have value -> init
            if (tmpInventoryAuditItems == null) {
                tmpInventoryAuditItems = new ArrayList<>();
            }

            // get information from form submit with action has value add
            Long productId = Long.parseLong(request.getParameter("productId"));
            Product product = productService.findProductById(productId);
            InventoryAuditItem inventoryAuditItem = new InventoryAuditItem();
            inventoryAuditItem.setProduct(product);

            // Check if product already exist in list, if exist -> send redirect to add page without add to list and show message "Product already exist in list"
            for (InventoryAuditItem tmpInventoryAuditItem : tmpInventoryAuditItems) {
                if (tmpInventoryAuditItem.getProduct().getId().equals(product.getId())) {
                    session.setAttribute("auditProductExistMessage", "Product already exist in list");
                    response.sendRedirect(request.getContextPath() + "/inventory-audits/add");
                    return;
                }
            }
            tmpInventoryAuditItems.add(inventoryAuditItem);

            // save again to session
            session.setAttribute("TMP_INVENTORY_AUDIT_ITEMS", tmpInventoryAuditItems);
            response.sendRedirect(request.getContextPath() + "/inventory-audits/add");

        } else if ("delete".equals(action)) {
            // get session
            HttpSession session = request.getSession();
            List<InventoryAuditItem> tmpInventoryAuditItems = (List<InventoryAuditItem>) session.getAttribute("TMP_INVENTORY_AUDIT_ITEMS");

            // Get information from form submit with action has value delete
            Long productId = Long.parseLong(request.getParameter("productId"));
            Product product = productService.findProductById(productId);

            for (InventoryAuditItem tmpInventoryAuditItem : tmpInventoryAuditItems) {
                if (tmpInventoryAuditItem.getProduct().getId().equals(product.getId())) {
                    tmpInventoryAuditItems.remove(tmpInventoryAuditItem);
                    break;
                }
            }
            // save again to session
            session.setAttribute("TMP_INVENTORY_AUDIT_ITEMS", tmpInventoryAuditItems);
            response.sendRedirect(request.getContextPath() + "/inventory-audits/add");

        } else if ("create".equals(action)) {
            System.out.println("OK");
        }
    }
}
