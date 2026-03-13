/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.storage;

import com.example.model.Storage;
import com.example.service.StorageService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "StorageList", urlPatterns = {"/specification/storage"})
public class StorageList extends HttpServlet {

    private StorageService s;
    private static final int PAGE_SIZE = 2;

    @Override
    public void init() {
        s = new StorageService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pageNo = 1;

        try {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        } catch (Exception e) {
        }

        int totalStorages = s.countStorage();
        int totalPages = (int) Math.ceil((double) totalStorages / PAGE_SIZE);

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }

        List<Storage> storages = s.getStorageByPage(pageNo, PAGE_SIZE);

        request.setAttribute("storages", storages);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/specification/storage/storageList.jsp")
                .forward(request, response);
    }

}
