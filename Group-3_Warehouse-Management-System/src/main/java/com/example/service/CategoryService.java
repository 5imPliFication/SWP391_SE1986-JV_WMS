package com.example.service;

import com.example.dao.CategoryDAO;
import com.example.model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public void createCategory(String name, String description, boolean isActive) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setIsActive(isActive ? 1 : 0);

        categoryDAO.createCategory(category);
    }

    public void updateCategory(Category category) {
        categoryDAO.updateCategory(category);
    }

    public Category getCategoryById(Long id) {
        return categoryDAO.getCategoryById(id);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    // Using for select box
    public List<Category> getActiveCategories() {
        return categoryDAO.getAllActive();
    }
}
