package com.example.service;

import com.example.dao.ModelDAO;
import com.example.model.Model;
import java.util.List;

public class ModelService {

    private ModelDAO m = new ModelDAO();

    public List<Model> getAllActiveModels() {
        return m.getAllActive();
    }

    public List<Model> getAllModels() {
        return m.getAll();
    }

    public List<Model> getModelByPage(int pageNo, int pageSize) {
        return m.getModelsByPage(pageNo, pageSize);
    }

    public Model getModelById(long modelId) {
        return m.getById(modelId);
    }

    public int countModel() {
        return m.count();
    }

    public void changeModelStatus(long id, boolean is_active) {
        m.updateModelStatus(id, is_active);
    }

    public boolean modelExists(String name, long brandId) {
        return m.existsByNameAndBrand(name, brandId);
    }

    public boolean CreateModel(String name, long BrandID, boolean active) {
        if (m.existsByNameAndBrand(name, BrandID)) {
            return false;
        }
        return m.createModel(name, BrandID, active);
    }
}
