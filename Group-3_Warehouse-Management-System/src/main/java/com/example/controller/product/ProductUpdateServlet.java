package com.example.controller.product;

import com.example.model.*;
import com.example.service.*;
import com.example.util.FileUtil;
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
@WebServlet("/products/update")
public class ProductUpdateServlet extends HttpServlet {

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

        long productId = Long.parseLong(request.getParameter("productId"));
        Product product = productService.findProductById(productId);

        request.setAttribute("product", product);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.setAttribute("models", models);
        request.setAttribute("chips", chips);
        request.setAttribute("rams", rams);
        request.setAttribute("storages", storages);
        request.setAttribute("sizes", sizes);
        request.setAttribute("units", units);

        request.getRequestDispatcher("/WEB-INF/product/product-update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getParameter("productId"));
        Product existingProduct = productService.findProductById(productId);

        Part imageFile = request.getPart("imageFile");
        String imgUrl = FileUtil.updateImage(imageFile, existingProduct.getImgUrl(), request);

        String productDescription = request.getParameter("productDescription");
        long brandId = Long.parseLong(request.getParameter("brandId"));
        long categoryId = Long.parseLong(request.getParameter("categoryId"));
        long modelId = Long.parseLong(request.getParameter("modelId"));
        long chipId = Long.parseLong(request.getParameter("chipId"));
        long ramId = Long.parseLong(request.getParameter("ramId"));
        long storageId = Long.parseLong(request.getParameter("storageId"));
        long sizeId = Long.parseLong(request.getParameter("sizeId"));
        long unitId = Long.parseLong(request.getParameter("unitId"));
        boolean productIsActive = Boolean.parseBoolean(request.getParameter("productIsActive"));

        if (productService.updateProduct(productDescription, imgUrl, brandId, categoryId, modelId, chipId, ramId, storageId, sizeId, unitId, productIsActive, productId)) {
            request.getSession().setAttribute("successMessage", "Product updated successfully");
        } else {
            request.getSession().setAttribute("errorMessage", "Product update failed");
        }
        response.sendRedirect("/products/update?productId=" + productId);
    }
}