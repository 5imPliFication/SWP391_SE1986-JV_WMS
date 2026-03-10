package com.example.controller.product;

import com.example.model.*;
import com.example.service.*;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

    private ProductService productService;
    private BrandService brandService;
    private CategoryService categoryService;
    private ModelService modelService;
    private ChipService chipService;
    private RamService ramService;
    private StorageService storageService;
    private SizeService sizeService;

    @Override
    public void init() {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String searchName = request.getParameter("searchName");
        String brandName = request.getParameter("brandName");
        String categoryName = request.getParameter("categoryName");
        String modelName = request.getParameter("modelName");
        String chipName = request.getParameter("chipName");
        String ramSize = request.getParameter("ramSize");
        String storageSize = request.getParameter("storageSize");
        String screenSize = request.getParameter("screenSize");

        String isActiveParam = request.getParameter("isActive");
        // Nếu parse sang boolean ngay sẽ lỗi All Status == false (parse("") => false)
        Boolean isActive = null;
        if (isActiveParam != null && !isActiveParam.isEmpty()) {
            isActive = Boolean.valueOf(isActiveParam);
        }

        // Using when the first time load this page (No param pageNo in URL)
        int pageNo = 1;
        // Get total record
        int totalProducts = productService.getTotalProducts(searchName, brandName, categoryName, modelName, chipName, ramSize, storageSize, screenSize, isActive);
        // Count total pages
        int totalPages = (int) Math.ceil((double) totalProducts / AppConstants.PAGE_SIZE);

        if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").isEmpty()) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        List<Product> products = productService.findAll(searchName, brandName, categoryName, modelName, chipName, ramSize, storageSize, screenSize, isActive, pageNo);
        List<Brand> brands = brandService.getActiveBrands();
        List<Category> categories = categoryService.getActiveCategories();
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
        request.getRequestDispatcher("/WEB-INF/product/product-list.jsp").forward(request, response);
    }
}