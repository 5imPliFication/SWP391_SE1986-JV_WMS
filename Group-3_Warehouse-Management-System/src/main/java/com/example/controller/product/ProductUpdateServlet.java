package com.example.controller.product;

import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Product;
import com.example.service.BrandService;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/products/update")
public class ProductUpdateServlet extends HttpServlet {

    private ProductService productService;
    private BrandService brandService;
    private CategoryService categoryService;

    @Override
    public void init() {
        productService = new ProductService();
        brandService = new BrandService();
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Brand> brands = brandService.getActiveBrands();
        List<Category> categories = categoryService.getActiveCategories();
        long productId = Long.parseLong(request.getParameter("productId"));
        Product product = productService.findProductById(productId);

        request.setAttribute("product", product);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/product/product-update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        String productDescription = request.getParameter("productDescription");
        String imgUrl = request.getParameter("imgUrl");
        long brandId = Long.parseLong(request.getParameter("brandId"));
        long categoryId = Long.parseLong(request.getParameter("categoryId"));
        boolean productIsActive = Boolean.parseBoolean(request.getParameter("isActive"));

        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        String searchName = request.getParameter("searchName");
        String brandName = request.getParameter("brandName");
        String categoryName = request.getParameter("categoryName");
        String isActive = request.getParameter("isActive");

        if (productService.updateProduct(productId, productName, productDescription, imgUrl, brandId, categoryId, productIsActive)) {
            request.getSession().setAttribute("successMessage", "Product updated successfully");
        } else {
            request.getSession().setAttribute("errorMessage", "Product update failed");
        }
        response.sendRedirect("/products/update?productId=" + productId + "&pageNo=" + pageNo + "&searchName=" + searchName
                + "&brandName=" + brandName + "&categoryName=" + categoryName + "&isActive=" + isActive);
    }
}