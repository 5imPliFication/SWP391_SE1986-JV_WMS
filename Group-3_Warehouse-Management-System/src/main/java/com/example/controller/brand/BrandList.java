/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.brand;

import com.example.model.Brand;
import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.BrandService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "BrandList", urlPatterns = {"/brands"})
public class BrandList extends HttpServlet {

    private BrandService b;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        b = new BrandService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pageNo = 1;
        int pageSize = 6;

        String pageParam = request.getParameter("pageNo");
        if (pageParam != null) {
            pageNo = Integer.parseInt(pageParam);
        }

        List<Brand> brandList = b.getBrandByPage(pageNo, pageSize);
        int totalRecords = b.getTotalBrands();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("brandList", brandList);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/product/brand/brand-list.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            boolean isActive = request.getParameter("active") != null && request.getParameter("active").equals("true");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            Brand brand = new Brand();
            brand.setName(name);
            brand.setDescription(description);
            brand.setActive(isActive);

            boolean checkName = b.addBrand(brand);

            if (!checkName) {
                response.sendRedirect("brands?status=name_existed");
                return;
            }
            activityLogService.log(user, "Add new brand");
            response.sendRedirect("brands?status=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("brands?status=error");
        }
    }
}
