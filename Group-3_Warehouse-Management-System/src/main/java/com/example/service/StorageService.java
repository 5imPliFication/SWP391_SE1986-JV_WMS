/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dao.StorageDAO;
import com.example.model.Storage;
import java.util.List;

/**
 *
 * @author PC
 */
public class StorageService {

    private StorageDAO s = new StorageDAO();

    public List<Storage> getAllActiveStorage() {
        return s.getAllActive();
    }

    public List<Storage> getAllStorage() {
        return s.getAll();
    }

    public List<Storage> getStorageByPage(int pageNo, int pageSize) {
        return s.getStoragesByPage(pageNo, pageSize);
    }

    public int countStorage() {
        return s.count();
    }

    public void changeStorageStatus(long id, boolean is_active) {
        s.updateStorageStatus(id, is_active);
    }
}
