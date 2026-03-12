package com.example.service;

import com.example.dao.ModelDAO;
import com.example.model.Brand;
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
}
