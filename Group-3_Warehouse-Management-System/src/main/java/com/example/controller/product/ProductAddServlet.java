package com.example.controller.product;

import com.example.model.*;
import com.example.service.*;
import com.example.util.FileUtil;
import jakarta.mail.Store;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@MultipartConfig
@WebServlet("/products/add")
public class ProductAddServlet extends HttpServlet {

    private ProductService productService;
    private BrandService brandService;
    private CategoryService categoryService;
    private ModelService modelService;
    private ChipService chipService;
    private RamService ramService;
    private StorageService storageService;
    private SizeService sizeService;
    private UnitService unitService;
    private ActivityLogService activityLogService;

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
        unitService = new UnitService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Brand> brands = brandService.getActiveBrands();
        List<Category> categories = categoryService.getActiveCategories();
        List<Model> models = modelService.getAllActiveModels();
        List<Chip> chips = chipService.getAllActiveChips();
        List<Ram> rams = ramService.getAllActiveRams();
        List<Storage> storages = storageService.getAllActiveStorage();
        List<Size> sizes = sizeService.getAllActiveSize();
        List<Unit> units = unitService.getAllUnit();

        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.setAttribute("models", models);
        request.setAttribute("chips", chips);
        request.setAttribute("rams", rams);
        request.setAttribute("storages", storages);
        request.setAttribute("sizes", sizes);
        request.setAttribute("units", units);
        request.getRequestDispatcher("/WEB-INF/product/product-add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Part imageFile = request.getPart("imageFile");
        String imgUrl = FileUtil.saveFile(imageFile, request);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String description = request.getParameter("productDescription");
        long brandId = Long.parseLong(request.getParameter("brandId"));
        long categoryId = Long.parseLong(request.getParameter("categoryId"));
        long modelId = Long.parseLong(request.getParameter("modelId"));
        long chipId = Long.parseLong(request.getParameter("chipId"));
        long ramId = Long.parseLong(request.getParameter("ramId"));
        long storageId = Long.parseLong(request.getParameter("storageId"));
        long sizeId = Long.parseLong(request.getParameter("sizeId"));
        long unitId = Long.parseLong(request.getParameter("unitId"));

        try {
            productService.addProduct(description, imgUrl, brandId, categoryId, modelId, chipId, ramId, storageId, sizeId, unitId);
            activityLogService.log(user, "Create product");
            request.getSession().setAttribute("successMessage", "Product added successfully");
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect("/products/add");
    }
}
