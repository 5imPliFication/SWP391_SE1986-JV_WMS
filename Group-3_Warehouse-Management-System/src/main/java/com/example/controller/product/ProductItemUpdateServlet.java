package com.example.controller.product;

import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductItem;
import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.BrandService;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/products/items/update")
public class ProductItemUpdateServlet extends HttpServlet {

    private ProductService productService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        productService = new ProductService();
        activityLogService = new ActivityLogService();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productItemId = Long.parseLong(request.getParameter("productItemId"));
        String btnAction = request.getParameter("btnChangeStatus");
        if("unavailable".equals(btnAction)) {
            productService.updateProductItem(productItemId, false);
        } else if ("available".equals(btnAction)) {
            productService.updateProductItem(productItemId, true);
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String productId = request.getParameter("productId");
        String pageNo = request.getParameter("pageNo");
        String searchSerial = request.getParameter("searchSerial");
        String isActive = request.getParameter("isActive");

        response.sendRedirect("/products/items?productId=" + productId + "&pageNo=" + pageNo + "&searchSerial=" + searchSerial + "&isActive=" + isActive);
    }
}
