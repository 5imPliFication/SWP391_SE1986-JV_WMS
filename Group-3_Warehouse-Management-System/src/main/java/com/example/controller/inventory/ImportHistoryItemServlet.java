package com.example.controller.inventory;

import com.example.dto.ImportHistoryDTO;
import com.example.service.GoodsHistoryService;
import com.example.util.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/inventory/import/history")
public class ImportHistoryItemServlet extends HttpServlet {

    private final GoodsHistoryService goodsHistoryService = new GoodsHistoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get pageNo
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = 1;
        if (pageNoStr != null && !pageNoStr.isEmpty()) {
            try {
                pageNo = Integer.parseInt(pageNoStr);
            } catch (NumberFormatException e) {
                pageNo = 1;
            }
        }

        // get date
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        // get data through service
        List<ImportHistoryDTO> importHistories = goodsHistoryService.getImportHistory(fromDate, toDate, pageNo);

        // count total records
        int totalRecords = goodsHistoryService.countImportHistory(fromDate, toDate);

        // calc total pages
        int totalPages = (int) Math.ceil((double) totalRecords / UserConstant.PAGE_SIZE);

        // set data
        request.setAttribute("importHistories", importHistories);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        // forward to jsp
        request.getRequestDispatcher("/WEB-INF/inventory/import-history.jsp").forward(request, response);
    }
}
