/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.chip;

import com.example.model.Chip;
import com.example.service.ChipService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "ChipList", urlPatterns = {"/specification/chip"})
public class ChipList extends HttpServlet {

    private ChipService c;
    private static final int PAGE_SIZE = 5;

    @Override
    public void init() {
        c = new ChipService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pageNo = 1;

        try {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        } catch (Exception e) {
        }

        int totalChips = c.countChip();
        int totalPages = (int) Math.ceil((double) totalChips / PAGE_SIZE);

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }

        List<Chip> chips = c.getChipByPage(pageNo, PAGE_SIZE);

        request.setAttribute("chips", chips);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/specification/chip/chipList.jsp")
                .forward(request, response);
    }

}
