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
}
