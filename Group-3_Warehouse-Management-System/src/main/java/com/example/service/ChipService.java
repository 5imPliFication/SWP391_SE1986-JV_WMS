package com.example.service;

import com.example.dao.ChipDAO;
import com.example.model.Chip;
import com.example.model.Model;

import java.util.List;

public class ChipService {

    private ChipDAO c = new ChipDAO();

    public List<Chip> getAllActiveChips() {
        return c.getAllActive();
    }

    public List<Chip> getAllChips() {
        return c.getAll();
    }

    public List<Chip> getChipByPage(int pageNo, int pageSize) {
        return c.getChipsByPage(pageNo, pageSize);
    }

    public Chip getChipById(long chipId) {
        return c.getById(chipId);
    }

    public int countChip() {
        return c.count();
    }

    public void changeChipStatus(long id, boolean is_active) {
        c.updateChipStatus(id, is_active);
    }

    public boolean CreateChip(String name, boolean active) {
        return c.createChip(name, active);
    }
}
