/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.config.DBConfig;
import com.example.dao.BrandDAO;
import com.example.model.Brand;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // Using for select box
    public List<Brand> getActiveBrands() {
        return b.getAllActive();
    }
  
    public void updateStatus(long id, boolean status) {
        b.changeStatus(id, status);
    }

    public List<Brand> getActiveBrand() {
        return b.findActiveBrand();
    }

    public Brand getBrandByID(Long id) {
        return b.findBrandByID(id);
    }

    public boolean updateBrand(Brand brand) {
        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            conn.setAutoCommit(false);
            if (b.isBrandExist(brand.getName())) {
                return false;
            }
            b.updateBrand(conn, brand);

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Update Brand failed", e);
        }
        return true;

    }
}
