package com.example.service;

import com.example.dao.*;
import com.example.dto.ProductDTO;
import com.example.model.*;
import com.example.util.AppConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private BrandDAO brandDAO = new BrandDAO();
    private ModelDAO modelDAO = new ModelDAO();
    private ChipDAO chipDAO = new ChipDAO();
    private RamDAO ramDAO = new RamDAO();
    private StorageDAO storageDAO = new StorageDAO();
    private SizeDAO sizeDAO = new SizeDAO();
    private UnitDAO unitDAO = new UnitDAO();

    public List<Product> findAll(String searchName, Long brandId, Long categoryId, Long modelId, Long chipId,
                                 Long ramId, Long storageId, Long sizeId, Boolean isActive, int pageNo)
    {
        return productDAO.getAll(searchName, brandId, categoryId, modelId, chipId, ramId, storageId, sizeId, isActive, pageNo);
    }

    public List<Product> findAll() {
        return productDAO.getAllProducts();
    }

    public int countAvailableProductsForOrder() {
        return productDAO.countAvailableProductsForOrder();
    }

    public List<Product> findAvailableProductsForOrder(int pageNo, int pageSize) {
        return productDAO.getAvailableProductsForOrder(pageNo, pageSize);
    }

    public int getTotalProducts(String searchName, Long brandId, Long categoryId, Long modelId, Long chipId,
                                Long ramId, Long storageId, Long sizeId, Boolean isActive) {
        return productDAO.countProducts(searchName, brandId, categoryId, modelId, chipId, ramId, storageId, sizeId, isActive);
    }

    public void addProduct(String productDescription, String imgUrl, long brandId,
            long categoryId, long modelId, long chipId, long ramId, long storageId, long screenSizeId, long unitId) {
        Brand brand = brandDAO.findById(brandId);
        Category category = categoryDAO.findById(categoryId);
        Model model = modelDAO.getById(modelId);
        Chip chip = chipDAO.getById(chipId);
        Ram ram = ramDAO.getById(ramId);
        Storage storage = storageDAO.getById(storageId);
        Size size = sizeDAO.getById(screenSizeId);
        Unit unit = unitDAO.getById(unitId);

        // Generate product name based on the attributes
        String productName = String.format("%s %s %s %s %s %s",
                brand.getName(),
                model.getName(),
                chip.getName(),
                ram.getSize(),
                storage.getSize(),
                size.getSize());

        // Check if a product with the same name already exists
        if(productDAO.existedByName(productName)) {
            throw new IllegalArgumentException("A product with the same name already exists: " + productName);
        }

        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        product.setImgUrl(imgUrl);
        product.setBrand(brand);
        product.setCategory(category);
        product.setModel(model);
        product.setChip(chip);
        product.setRam(ram);
        product.setStorage(storage);
        product.setSize(size);
        product.setUnit(unit);

        productDAO.create(product);
    }

    public Product findProductById(long productId) {
        return productDAO.findById(productId);
    }

    public boolean updateProduct(String productDescription, String imgUrl, boolean isActive, long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setDescription(productDescription);
        product.setImgUrl(imgUrl);
        product.setIsActive(isActive);

        return productDAO.update(product);
    }

    public List<ProductItem> findItemsByProductId(long productId, String searchSerial, Boolean isActive, int pageNo) {
        return productDAO.getItemsByProductId(productId, searchSerial, isActive, pageNo);
    }

    public int countProductItems(long productId, String searchSerial, Boolean isActive) {
        return productDAO.countProductItems(productId, searchSerial, isActive);
    }

    public ProductItem findProductItemById(long productItemId) {
        return productDAO.findItemById(productItemId);
    }

    public boolean updateProductItem(long productItemId, Boolean isActive) {
        ProductItem productItem = new ProductItem();
        productItem.setId(productItemId);
        productItem.setIsActive(isActive);

        return productDAO.updateItem(productItem);
    }

    public Map<String, Object> getOutOfStockAlertProducts(String name, int pageNo) {

        Map<String, Object> results = new HashMap<>();

        int offset = (pageNo - 1) * AppConstants.PAGE_SIZE;
        List<ProductDTO> products = productDAO.getLowStockProducts(name, offset);

        // handle pagination
        int totalProducts = getTotalProducts(name);
        int totalPages = (int) Math.ceil((double) totalProducts / AppConstants.PAGE_SIZE);

        // set value for result
        results.put("products", products);
        results.put("totalPages", totalPages);
        return results;
    }

    private int getTotalProducts(String name) {
        return productDAO.countTotalProducts(name);
    }
    // Count all products
    public int countAllProducts() {
        return productDAO.countAll();
    }

    // Count low stock products
    public int countLowStockProducts(int threshold) {
        return productDAO.countLowStock(threshold);
    }

    // Get low stock products
    public List<Product> getLowStockProducts(int threshold) {
        return productDAO.findLowStock(threshold);
    }

    // update current price of product by product id
    public boolean updateCurrentPriceByProductId(long productId, double newPrice) {
        return productDAO.updateCurrentPriceByProductId(productId, newPrice);
    }
}