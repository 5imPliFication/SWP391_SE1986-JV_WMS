/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.specification;

import com.example.service.ChipService;
import com.example.service.ModelService;
import com.example.service.RamService;
import com.example.service.SizeService;
import com.example.service.StorageService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author PC
 */
@WebServlet(name = "SpecificationList", urlPatterns = {"/specification"})
public class SpecificationList extends HttpServlet {

    private ModelService m;
    private ChipService c;
    private RamService r;
    private StorageService sto;
    private SizeService s;

    @Override
    public void init() {
        m = new ModelService();
        c = new ChipService();
        r = new RamService();
        sto = new StorageService();
        s = new SizeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("models", m.getAllModels());
        request.setAttribute("chips", c.getAllChips());
        request.setAttribute("rams", r.getAllRams());
        request.setAttribute("storages", sto.getAllStorage());
        request.setAttribute("sizes", s.getAllSizes());

        request.getRequestDispatcher("/WEB-INF/product/specification/specificationList.jsp")
                .forward(request, response);

    }

}
