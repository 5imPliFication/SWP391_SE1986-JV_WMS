package com.example.service;

import com.example.dao.CategoryDAO;
import com.example.model.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    // Using for select box
    public List<Category> getActiveCategories() {
        return categoryDAO.getAllActive();
    }
}
