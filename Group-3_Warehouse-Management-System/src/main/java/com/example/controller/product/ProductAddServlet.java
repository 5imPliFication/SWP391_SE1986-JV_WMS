package com.example.controller.product;

import com.example.model.*;
import com.example.service.*;
import jakarta.mail.Store;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        Part filePart = request.getPart("imageFile");
        String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
        System.out.println("Received file: " + fileName);

        // SAVE TMP TO /target --> load image right after upload, but the image will be lost after redeploy
        String targetPath = request.getServletContext().getRealPath("/static/images/products");
        File targetDir = new File(targetPath);
        if (!targetDir.exists()) targetDir.mkdirs();

        // SAVE FOREVER TO PROJECT --> use for redeploy, but can't load image right after upload
        // 1. Get the real path of the deployed application (D:\SWP391_SE1986-JV_WMS\Group-3_Warehouse-Management-System\target\Group-3_Warehouse-Management-System)
        String deployedPath = request.getServletContext().getRealPath("");
        // 2. Navigate up to the project root (D:\SWP391_SE1986-JV_WMS\Group-3_Warehouse-Management-System)
        String projectRoot = new File(deployedPath).getParentFile().getParentFile().getAbsolutePath();
        // 3. Create the upload path (D:\SWP391_SE1986-JV_WMS\Group-3_Warehouse-Management-System\src\main\webapp\static\images\products)
        String uploadPath = projectRoot + File.separator + "src" + File.separator + "main" + File.separator +
                "webapp" + File.separator + "static" + File.separator + "images" + File.separator + "products";
        // Create the directory for saving image if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        System.out.println("Target path: " + targetPath);
        System.out.println("Upload path: " + uploadPath);

        String fullTargetPath = targetPath + File.separator + fileName;
        String fullUploadPath = uploadPath + File.separator + fileName;

        // Save the uploaded file to /target (File.separator: Windows -> \)
        filePart.write(fullTargetPath);
        // Copy the file from /target to project
        Files.copy(
                Paths.get(fullTargetPath),
                Paths.get(fullUploadPath),
                StandardCopyOption.REPLACE_EXISTING
        );

        //Store the relative path to the image in the database
        String imgUrl = "/static/images/products/" + fileName;
        System.out.println("Image URL: " + imgUrl);

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
            request.getSession().setAttribute("successMessage", "Product added successfully");
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect("/products/add");
    }
}