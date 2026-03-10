package com.example.service;

import com.example.dao.RamDAO;
import com.example.model.Ram;
import java.util.List;

public class RamService {

    private RamDAO ramDAO = new RamDAO();

    public List<Ram> getAllActiveRams() {
        return ramDAO.getAllActive();
    }
}
