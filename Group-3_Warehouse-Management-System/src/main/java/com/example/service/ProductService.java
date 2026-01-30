package com.example.service;

import com.example.dao.ProductDAO;
import com.example.model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public List<Product> findAll(String searchName, String brandName, String categoryName, int pageNo) {
        return productDAO.getAll(searchName, brandName, categoryName, pageNo);
    }

    public int getTotalProducts(String searchName, String brandName, String categoryName) {
        return productDAO.countProducts(searchName, brandName, categoryName);
    }
}
