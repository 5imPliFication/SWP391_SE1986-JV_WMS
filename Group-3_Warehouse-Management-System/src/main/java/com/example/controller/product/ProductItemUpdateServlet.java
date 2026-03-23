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
//        double productItemCurrentPrice = Double.parseDouble(request.getParameter("productItemCurrentPrice"));
        boolean productItemIsActive = Boolean.parseBoolean(request.getParameter("productItemIsActive"));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String productId = request.getParameter("productId");
        String pageNo = request.getParameter("pageNo");
        String searchSerial = request.getParameter("searchSerial");
        String isActive = request.getParameter("isActive");

        if (productService.updateProductItem(productItemId, productItemIsActive)) {
            activityLogService.log(user, "Update product item");
            request.getSession().setAttribute("successMessage", "Product item updated successfully");
        } else {
            request.getSession().setAttribute("errorMessage", "Product item update failed");
        }
        response.sendRedirect("/products/items?productId=" + productId + "&pageNo=" + pageNo + "&searchSerial=" + searchSerial + "&isActive=" + isActive);
    }
}
