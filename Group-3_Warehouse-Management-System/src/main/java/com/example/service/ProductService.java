package com.example.service;

import com.example.dao.ProductDAO;
import com.example.model.Product;
import com.example.model.ProductItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    // init productDAO
    private final ProductDAO productDAO = new ProductDAO();

    // search product by name
    public List<Product> findProductByName(String name) {
        return productDAO.findProductByName(name);
    }

    // save list product items
    public boolean saveProductItems(String[] serials, String[] prices, String[] productIds) {

        // init list product items
        List<ProductItem> productItems = new ArrayList<>();
        try {
            // loop
            for (int i = 1; i <= serials.length; i++) {
                String serial = serials[i - 1];
                if(productDAO.isExistSerial(serial)) {
                    return false;
                }
                double price = Double.parseDouble(prices[i - 1]);
                if(price < 0) {
                    return false;
                }
                Long productId = Long.parseLong(productIds[i - 1]);
                productItems.add(new ProductItem(serial, price, LocalDateTime.now(), productId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return productDAO.saveProductItems(productItems);
    }
}
