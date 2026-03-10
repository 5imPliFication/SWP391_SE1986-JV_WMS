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
}
