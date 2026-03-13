/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.size;

import com.example.model.Size;
import com.example.service.SizeService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author PC
 */
@WebServlet(name = "SizeList", urlPatterns = {"/specification/size"})
public class SizeList extends HttpServlet {

    private SizeService s;
    private static final int PAGE_SIZE = 5;

    @Override
    public void init() {
        s = new SizeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pageNo = 1;

        try {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        } catch (Exception e) {
        }

        int totalSizes = s.countSize();
        int totalPages = (int) Math.ceil((double) totalSizes / PAGE_SIZE);

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }

        List<Size> sizes = s.getSizeByPage(pageNo, PAGE_SIZE);

        request.setAttribute("sizes", sizes);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/specification/size/sizeList.jsp")
                .forward(request, response);
    }
}
