/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dao.BrandDAO;
import com.example.model.Brand;
import java.util.List;

/**
 *
 * @author PC
 */
public class BrandService {

    private BrandDAO b;

    public BrandService() {
        b = new BrandDAO();
    }

    public List<Brand> getAllBrand() {
        return b.findAll();
    }

    public boolean addBrand(Brand brand) {
        if (b.isBrandExist(brand.getName())) {
            return false;
        }
        b.addBrand(brand);
        return true;
    }

}
