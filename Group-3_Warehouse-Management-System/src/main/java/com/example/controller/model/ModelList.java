/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.model;

import com.example.model.Model;
import com.example.service.BrandService;
import com.example.service.ModelService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author PC
 */
@WebServlet(name = "ModelList", urlPatterns = {"/specification/model"})
public class ModelList extends HttpServlet {

    private ModelService m;
    private BrandService b;
    private static final int PAGE_SIZE = 5;

    @Override
    public void init() {
        m = new ModelService();
        b = new BrandService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int pageNo = 1;

        try {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        } catch (Exception e) {
        }

        int totalModels = m.countModel();
        int totalPages = (int) Math.ceil((double) totalModels / PAGE_SIZE);

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute("flashModelCreateError");
            if (flash != null) {
                request.setAttribute("error", flash);
                session.removeAttribute("flashModelCreateError");
            }
        }

        List<Model> models = m.getModelByPage(pageNo, PAGE_SIZE);

        request.setAttribute("models", models);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("brands", b.getActiveBrands());

        request.getRequestDispatcher("/WEB-INF/specification/model/modelList.jsp")
                .forward(request, response);
    }
}
