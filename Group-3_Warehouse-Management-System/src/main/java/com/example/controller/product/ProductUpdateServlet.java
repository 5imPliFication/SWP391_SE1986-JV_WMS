package com.example.controller.product;

import com.example.model.Product;
import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.ProductService;
import com.example.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@MultipartConfig
@WebServlet("/products/update")
public class ProductUpdateServlet extends HttpServlet {

    private ProductService productService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        productService = new ProductService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long productId = Long.parseLong(request.getParameter("productId"));
        Product product = productService.findProductById(productId);

        request.setAttribute("product", product);

        request.getRequestDispatcher("/WEB-INF/product/product-update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getParameter("productId"));
        Product existingProduct = productService.findProductById(productId);

        Part imageFile = request.getPart("imageFile");
        String imgUrl = FileUtil.updateImage(imageFile, existingProduct.getImgUrl(), request);

        String productDescription = request.getParameter("productDescription");
        boolean productIsActive = Boolean.parseBoolean(request.getParameter("productIsActive"));
        double currentPrice = Double.parseDouble(request.getParameter("productCurrentPrice"));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Redirect exactly previous product page
        String pageNo = request.getParameter("pageNo");
        String searchName = request.getParameter("searchName");
        String brandId = request.getParameter("brandId");
        String categoryId = request.getParameter("categoryId");
        String modelId = request.getParameter("modelId");
        String chipId = request.getParameter("chipId");
        String ramId = request.getParameter("ramId");
        String storageId = request.getParameter("storageId");
        String sizeId = request.getParameter("sizeId");
        String isActive = request.getParameter("isActive");

        if (productService.updateProduct(productDescription, imgUrl, productIsActive, productId) && productService.updateCurrentPriceByProductId(productId, currentPrice)) {
            activityLogService.log(user, "Update product");
            request.getSession().setAttribute("successMessage", "Product updated successfully");
        } else {
            request.getSession().setAttribute("errorMessage", "Product update failed");
        }
        response.sendRedirect("/products?pageNo=" + pageNo + "&searchName=" + searchName + "&brandId=" + brandId + "&categoryId=" + categoryId + "&modelId=" + modelId + "&chipId=" + chipId + "&ramId=" + ramId + "&storageId=" + storageId + "&sizeId=" + sizeId + "&isActive=" + isActive);
    }
}
