/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.brand;

import com.example.model.Brand;
import com.example.service.BrandService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "BrandList", urlPatterns = {"/brand"})
public class BrandList extends HttpServlet {

    private BrandService b;

    @Override
    public void init() {
        b = new BrandService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Brand> list = b.getAllBrand();
        request.setAttribute("brandList", list);
        request.getRequestDispatcher("/WEB-INF/product/brand/brand-list.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            boolean isActive = request.getParameter("active") != null && request.getParameter("active").equals("true");

            Brand brand = new Brand();
            brand.setName(name);
            brand.setDescription(description);
            brand.setActive(isActive);

            boolean checkName = b.addBrand(brand);

            if (!checkName) {
                response.sendRedirect("brand?status=name_existed");
                return;
            }
            response.sendRedirect("brand?status=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("brand?status=error");
        }
    }
}
