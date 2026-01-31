package com.example.controller.inventory;

import com.example.model.Product;
import com.example.model.ProductItem;
import com.example.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/import-products")
public class ImportProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get action
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("search".equals(action)) {
            String name = request.getParameter("name");

            // set search name product for session
            session.setAttribute("searchName", name);
            // call service to get list products
            List<Product> products = productService.searchProductByName(name);
            if (products == null || products.isEmpty()) {
                request.setAttribute("error", "Product not found. Please add new product");
            } else {
                request.setAttribute("products", products);
            }
        } else if ("delete".equals(action)) {
            // get session
            List<String> product_items = (List<String>) session.getAttribute("IMPORT_ITEMS");
            // get id need delete
            if (product_items != null) {
                int index = Integer.parseInt(request.getParameter("index"));
                product_items.remove(index);
            }
            response.sendRedirect(request.getContextPath() + "/import-products");
            return;
        } else {
            // view or refresh
            String searchName = (String) session.getAttribute("searchName");
            // if exist -> view again
            if (searchName != null) {
                List<Product> products = productService.searchProductByName(searchName);
                request.setAttribute("products", products);
            }
        }
        request.getRequestDispatcher("/WEB-INF/inventory/import-products.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get action
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            // get session
            HttpSession session = request.getSession();
            List<String> product_items = (List<String>) session.getAttribute("IMPORT_ITEMS");

            // if null -> init
            if (product_items == null) {
                product_items = new ArrayList<>();
            }
            // get name
            String productName = request.getParameter("product-name");
            // set name for session
            product_items.add(productName);

            // set attribute for session
            session.setAttribute("IMPORT_ITEMS", product_items);
            response.sendRedirect(request.getContextPath() + "/import-products");
        } else if ("save".equals(action)) {
            // get session
            HttpSession session = request.getSession();
            List<String> product_items = (List<String>) session.getAttribute("IMPORT_ITEMS");
            // get amount of record input product item
            int amountRecord = product_items.size();

            // init list product items
            List<ProductItem> productItems = new ArrayList<>();

            // loop
            for (int i = 1; i <= amountRecord; i++) {
                // get value of product items
                String serial = request.getParameter("serial");
                String productName = request.getParameter("product-name");
                Double price = Double.parseDouble(request.getParameter("price"));
                Long id = productService.findProductIdByName(productName);
                productItems.add(new ProductItem(serial, price, id, LocalDateTime.now()));
            }
            productService.saveProductItems(productItems);
            response.sendRedirect(request.getContextPath() + "/import-products");
        }
    }
}
