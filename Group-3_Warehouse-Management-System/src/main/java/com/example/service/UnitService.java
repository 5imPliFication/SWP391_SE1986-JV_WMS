/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dao.SizeDAO;
import com.example.dao.UnitDAO;
import com.example.model.Size;
import com.example.model.Unit;

import java.util.List;

/**
 *
 * @author PC
 */
public class UnitService {

    private UnitDAO unitDAO = new UnitDAO();

    public List<Unit> getAllUnit() {
        return unitDAO.getAll();
    }
    public Unit getUnitById(long sizeId) {
        return unitDAO.getById(sizeId);
    }
}
