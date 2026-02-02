/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.brand;

import com.example.model.Brand;
import com.example.service.BrandService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BrandUpdate", urlPatterns = {"/brand-update"})
public class BrandUpdate extends HttpServlet {

    private BrandService b;

    @Override
    public void init() {
        b = new BrandService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long brandId = Long.parseLong(request.getParameter("brandId"));
        Brand brand = b.getBrandByID(brandId);
        request.setAttribute("brand", brand);
        request.getRequestDispatcher("/WEB-INF/product/brand/update-brand.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String name = request.getParameter("name").trim();
            String description = request.getParameter("description");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            Brand brand = new Brand();
            brand.setId(id);
            brand.setName(name);
            brand.setDescription(description);
            brand.setActive(status);

            boolean success = b.updateBrand(brand);

            if (!success) {
                request.setAttribute("error", "Tên brand đã tồn tại");
                request.setAttribute("brand", brand); 
                request.getRequestDispatcher("/WEB-INF/product/brand/update-brand.jsp").forward(request, response);
                return;
            }
            response.sendRedirect("brands?status=update_success");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
