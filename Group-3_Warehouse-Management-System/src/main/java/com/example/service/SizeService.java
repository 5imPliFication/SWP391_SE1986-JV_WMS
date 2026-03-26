/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dao.SizeDAO;
import com.example.model.Size;
import java.util.List;

/**
 *
 * @author PC
 */
public class SizeService {

    private SizeDAO s = new SizeDAO();

    public List<Size> getAllActiveSize() {
        return s.getAllActive();
    }

    public List<Size> getAllSizes() {
        return s.getAll();
    }

    public List<Size> getSizeByPage(int pageNo, int pageSize) {
        return s.getSizesByPage(pageNo, pageSize);
    }

    public Size getSizeById(long sizeId) {
        return s.getById(sizeId);
    }

    public int countSize() {
        return s.count();
    }

    public void changeSizeStatus(long id, boolean is_active) {
        s.updateSizeStatus(id, is_active);
    }

    public boolean sizeExists(String size) {
        return s.existsBySize(size);
    }

    public boolean CreateSize(String size, boolean active) {
        if (s.existsBySize(size)) {
            return false;
        }
        return s.createSize(size, active);
    }
}
