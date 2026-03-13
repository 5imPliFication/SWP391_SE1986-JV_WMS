package com.example.service;

import com.example.dao.RamDAO;
import com.example.model.Model;
import com.example.model.Ram;
import java.util.List;

public class RamService {

    private RamDAO r = new RamDAO();

    public List<Ram> getAllActiveRams() {
        return r.getAllActive();
    }

    public List<Ram> getAllRams() {
        return r.getAll();
    }

    public List<Ram> getRamByPage(int pageNo, int pageSize) {
        return r.getRamsByPage(pageNo, pageSize);
    }

    public Ram getRamById(long ramId) {
        return r.getById(ramId);
    }

    public int countRam() {
        return r.count();
    }

    public void changeRamStatus(long id, boolean is_active) {
        r.updateRamStatus(id, is_active);
    }
}
