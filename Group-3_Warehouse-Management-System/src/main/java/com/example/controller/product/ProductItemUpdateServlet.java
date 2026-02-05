package com.example.controller.product;

import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductItem;
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

@WebServlet("/products/items/update")
public class ProductItemUpdateServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Get product item
        long productItemId = Long.parseLong(request.getParameter("productItemId"));
        ProductItem productItem = productService.findProductItemById(productItemId);

        request.setAttribute("productItem", productItem);
        request.getRequestDispatcher("/WEB-INF/product/item/product-item-update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productItemId = Long.parseLong(request.getParameter("productItemId"));
        double productItemCurrentPrice = Double.parseDouble(request.getParameter("productItemCurrentPrice"));
        boolean productItemIsActive = Boolean.parseBoolean(request.getParameter("productItemIsActive"));

        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        String searchSerial =  request.getParameter("searchSerial");
        String isActiveParam = request.getParameter("isActive");
        String isActive = request.getParameter("isActive");

        if(productService.updateProductItem(productItemId, productItemCurrentPrice, productItemIsActive)) {
            request.getSession().setAttribute("successMessage", "Product item updated successfully");
        } else {
            request.getSession().setAttribute("errorMessage", "Product item update failed");
        }
        response.sendRedirect("/products/items/update?productItemId=" + productItemId + "&pageNo=" + pageNo + "&searchSerial=" + searchSerial + "&isActive=" + isActive);
    }
}