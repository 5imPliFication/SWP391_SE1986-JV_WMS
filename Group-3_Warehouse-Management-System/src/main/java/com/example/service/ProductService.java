package com.example.service;

import com.example.dao.BrandDAO;
import com.example.dao.CategoryDAO;
import com.example.dao.ProductDAO;
import com.example.model.Brand;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductItem;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private BrandDAO brandDAO = new BrandDAO();

    public List<Product> findAll(String searchName, String brandName, String categoryName, Boolean isActive, int pageNo) {
        return productDAO.getAll(searchName, brandName, categoryName,isActive, pageNo);
    }

    public int getTotalProducts(String searchName, String brandName, String categoryName, Boolean isActive) {
        return productDAO.countProducts(searchName, brandName, categoryName, isActive);
    }

    public boolean addProduct(String productName, String productDescription, String imgUrl, long brandId, long categoryId) {
        Brand brand = brandDAO.findById(brandId);
        Category category = categoryDAO.findById(categoryId);

        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        product.setImgUrl(imgUrl);
        product.setBrand(brand);
        product.setCategory(category);

        return productDAO.create(product);
    }

    public Product findProductById(long productId) {
        return productDAO.findById(productId);
    }

    public boolean updateProduct(long productId, String productName, String productDescription, String imgUrl, long brandId, long categoryId, boolean isActive) {
        Brand brand = brandDAO.findById(brandId);
        Category category = categoryDAO.findById(categoryId);

        Product product = new Product();
        product.setId(productId);
        product.setName(productName);
        product.setDescription(productDescription);
        product.setImgUrl(imgUrl);
        product.setIsActive(isActive);
        product.setBrand(brand);
        product.setCategory(category);

        return productDAO.update(product);
    }

    public List<ProductItem> findItemsByProductId(long productId, String searchSerial, Boolean isActive, int pageNo) {
        return productDAO.getItemsByProductId(productId, searchSerial,isActive, pageNo);
    }

    public int countProductItems(long productId, String searchSerial, Boolean isActive) {
        return productDAO.countProductItems(productId, searchSerial, isActive);
    }

    public ProductItem findProductItemById(long productItemId) {
        return productDAO.findItemById(productItemId);
    }

    public boolean updateProductItem(long productItemId, double currentPrice, Boolean isActive) {
        ProductItem productItem = new ProductItem();
        productItem.setId(productItemId);
        productItem.setCurrentPrice(currentPrice);
        productItem.setIsActive(isActive);

        return productDAO.updateItem(productItem);
    }

}
