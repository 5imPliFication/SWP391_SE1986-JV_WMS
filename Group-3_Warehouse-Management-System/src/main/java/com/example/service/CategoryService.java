package com.example.service;


import com.example.dao.CategoryDAO;
import com.example.model.Category;


public class CategoryService {

    private final CategoryDAO categoryDAO;


    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }


    public void createCategory(String name, String description, boolean isActive) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setActive(isActive);

        categoryDAO.createCategory(category);
    }

    public Category getCategoryById(Long id) {
        return categoryDAO.getCategoryById(id);
    }

    public java.util.List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
}
