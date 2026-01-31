package com.example.service;

import com.example.dao.ProductDAO;
import com.example.model.Product;
import com.example.model.ProductItem;

import java.util.List;

public class ProductService {

    // init productDAO
    private final ProductDAO productDAO = new ProductDAO();

    // search product by name
    public List<Product> searchProductByName(String name) {
        return productDAO.searchProductByName(name);
    }

    // find id of product by name
    public Long findProductIdByName(String productName) {
        return productDAO.findProductIdByName(productName);
    }

    // save list product items
    public void saveProductItems(List<ProductItem> productItems) {
        if (productItems == null) {
            return;
        }
        productDAO.saveProductItems(productItems);
    }



}
