package com.example.controller.audit;

import com.example.model.*;
import com.example.service.*;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/inventory-audits/add")
@MultipartConfig
public class InventoryAuditAddServlet extends HttpServlet {

    private InventoryAuditService inventoryAuditService;
    private ProductService productService;
    private BrandService brandService;
    private CategoryService categoryService;
    private ModelService modelService;
    private ChipService chipService;
    private RamService ramService;
    private StorageService storageService;
    private SizeService sizeService;

    @Override
    public void init() throws ServletException {
        inventoryAuditService = new InventoryAuditService();
        productService = new ProductService();
        brandService = new BrandService();
        categoryService = new CategoryService();
        modelService = new ModelService();
        chipService = new ChipService();
        ramService = new RamService();
        storageService = new StorageService();
        sizeService = new SizeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchName = request.getParameter("searchName");
        Long categoryId  = request.getParameter("categoryId") != null && !request.getParameter("categoryId").isEmpty() ? Long.parseLong(request.getParameter("categoryId")) : null;
        Long brandId  = request.getParameter("brandId") != null && !request.getParameter("brandId").isEmpty() ? Long.parseLong(request.getParameter("brandId")) : null;
        Long modelId  = request.getParameter("modelId") != null && !request.getParameter("modelId").isEmpty() ? Long.parseLong(request.getParameter("modelId")) : null;
        Long chipId  = request.getParameter("chipId") != null && !request.getParameter("chipId").isEmpty() ? Long.parseLong(request.getParameter("chipId")) : null;
        Long ramId  = request.getParameter("ramId") != null && !request.getParameter("ramId").isEmpty() ? Long.parseLong(request.getParameter("ramId")) : null;
        Long storageId  = request.getParameter("storageId") != null && !request.getParameter("storageId").isEmpty() ? Long.parseLong(request.getParameter("storageId")) : null;
        Long sizeId  = request.getParameter("sizeId") != null && !request.getParameter("sizeId").isEmpty() ? Long.parseLong(request.getParameter("sizeId")) : null;

        String isActiveParam = request.getParameter("isActive");
        // Nếu parse sang boolean ngay sẽ lỗi All Status == false (parse("") => false)
        Boolean isActive = null;
        if (isActiveParam != null && !isActiveParam.isEmpty()) {
            isActive = Boolean.valueOf(isActiveParam);
        }

        // Using when the first time load this page (No param pageNo in URL)
        int pageNo = 1;
        // Get total record
        int totalProducts = productService.getTotalProducts(searchName, brandId, categoryId, modelId, chipId, ramId, storageId, sizeId, isActive);
        // Count total pages
        int totalPages = (int) Math.ceil((double) totalProducts / AppConstants.PAGE_SIZE);

        if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").isEmpty()) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        List<Product> products = productService.findAll(searchName, brandId, categoryId, modelId, chipId, ramId, storageId, sizeId, isActive, pageNo);
        List<Brand> brands = brandService.getAllBrands();
        List<Category> categories = categoryService.getAllCategories();
        List<Model> models = modelService.getAllModels();
        List<Chip> chips = chipService.getAllChips();
        List<Ram> rams = ramService.getAllRams();
        List<Storage> storages = storageService.getAllStorage();
        List<Size> sizes = sizeService.getAllSizes();

        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.setAttribute("models", models);
        request.setAttribute("chips", chips);
        request.setAttribute("rams", rams);
        request.setAttribute("storages", storages);
        request.setAttribute("sizes", sizes);
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
            // Get session
            HttpSession session = request.getSession();
            List<InventoryAuditItem> tmpInventoryAuditItems = (List<InventoryAuditItem>) session.getAttribute("TMP_INVENTORY_AUDIT_ITEMS");
            if(tmpInventoryAuditItems == null) {
                session.setAttribute("auditNoProductMessage", "No product in audit list");
                response.sendRedirect(request.getContextPath() + "/inventory-audits/add");
                return;
            } else {
                User user = (User) session.getAttribute("user");
                Long createdBy = user.getId();
                try {
                    inventoryAuditService.createFullAudit(createdBy, tmpInventoryAuditItems);
                    // remove session after create success
                    session.removeAttribute("TMP_INVENTORY_AUDIT_ITEMS");
                    session.setAttribute("auditCreatedSuccessMessage", "Inventory audit created successfully");
                    response.sendRedirect(request.getContextPath() + "/inventory-audits/add");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
