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
}
