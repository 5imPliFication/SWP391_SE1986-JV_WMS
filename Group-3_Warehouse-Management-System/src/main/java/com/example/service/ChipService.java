/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dao.ChipDAO;
import com.example.model.Chip;
import java.util.List;

public class ChipService {

    private ChipDAO c = new ChipDAO();

    public List<Chip> getAllActiveChips() {
        return c.getAllActive();
    }
}
