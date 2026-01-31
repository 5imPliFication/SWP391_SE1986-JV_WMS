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

        // get session
        HttpSession session = request.getSession();

        if ("search".equals(action)) {
            String name = request.getParameter("name");

            // set search name product for session
            session.setAttribute("searchName", name);
            // call service to get list products
            List<Product> products = productService.findProductByName(name);
            if (products == null || products.isEmpty()) {
                request.setAttribute("error", "Product not found. Please add new product");
            } else {
                request.setAttribute("products", products);
            }
        } else if ("delete".equals(action)) {
            // get session
            List<Product> products = (List<Product>) session.getAttribute("IMPORT_ITEMS");
            // get id need delete
            if (products != null) {
                int index = Integer.parseInt(request.getParameter("index"));
                products.remove(index);
            }
            response.sendRedirect(request.getContextPath() + "/import-products");
            return;
        } else {
            // view or refresh
            String searchName = (String) session.getAttribute("searchName");
            // if exist -> view again
            if (searchName != null) {
                List<Product> products = productService.findProductByName(searchName);
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
            List<Product> products = (List<Product>) session.getAttribute("IMPORT_ITEMS");

            // if null -> init
            if (products == null) {
                products = new ArrayList<>();
            }
            // get attribute product
            Long id = Long.parseLong(request.getParameter("product-id"));
            String name = request.getParameter("product-name");
            // set product for session
            products.add(new Product(id, name));

            // set attribute for session
            session.setAttribute("IMPORT_ITEMS", products);
            response.sendRedirect(request.getContextPath() + "/import-products");
        } else if ("save".equals(action)) {
            // get session
            HttpSession session = request.getSession();
            List<Product> products = (List<Product>) session.getAttribute("IMPORT_ITEMS");
            // get amount of record input product item
            int amountRecord = products.size();
            // get value
            String[] serials = request.getParameterValues("serial");
            String[] prices = request.getParameterValues("price");
            String[] productIds = request.getParameterValues("product-id");

            // init list product items
            List<ProductItem> productItems = new ArrayList<>();

            // loop
            for (int i = 1; i <= amountRecord; i++) {
                String serial = serials[i-1];
                Double price = Double.parseDouble(prices[i-1]);
                Long productId = Long.parseLong(productIds[i-1]);
                productItems.add(new ProductItem(serial, price, LocalDateTime.now(), productId));
            }
            productService.saveProductItems(productItems);

            // delete session after save success
            session.removeAttribute("IMPORT_ITEMS");
            response.sendRedirect(request.getContextPath() + "/import-products");
        }
    }
}
