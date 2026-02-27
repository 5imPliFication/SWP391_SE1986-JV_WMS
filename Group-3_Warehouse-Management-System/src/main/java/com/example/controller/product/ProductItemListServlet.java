package com.example.controller.product;

import com.example.model.Product;
import com.example.model.ProductItem;
import com.example.service.ProductService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/products/items")
public class ProductItemListServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long productId = Long.parseLong(request.getParameter("productId"));
        String searchSerial = request.getParameter("searchSerial");

        String isActiveParam = request.getParameter("isActive");
        // Nếu parse sang boolean ngay sẽ lỗi All Status == false (parse("") => false)
        Boolean isActive = null;
        if (isActiveParam != null && !isActiveParam.isEmpty()) {
            isActive = Boolean.valueOf(isActiveParam);
        }

        // Using when the first time load this page (No param pageNo in URL)
        int pageNo = 1;
        // Get total record
        int totalProductItems = productService.countProductItems(productId, searchSerial, isActive);
        // Count total pages
        int totalPages = (int) Math.ceil((double) totalProductItems / AppConstants.PAGE_SIZE);

        if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").isEmpty()) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        Product product = productService.findProductById(productId);
        List<ProductItem> productItems = productService.findItemsByProductId(productId, searchSerial, isActive, pageNo);
        request.setAttribute("productItems", productItems);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("product", product);
        // Use to determine active page number for the first time
        request.setAttribute("pageNo", pageNo);
        request.getRequestDispatcher("/WEB-INF/product/item/product-item-list.jsp").forward(request, response);
    }
}