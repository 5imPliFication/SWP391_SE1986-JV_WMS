package com.example.controller.inventory;

import com.example.dto.ImportHistoryDTO;
import com.example.dto.ImportHistoryDetailDTO;
import com.example.service.GoodsHistoryService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/inventory/import/history")
public class ImportHistoryItemServlet extends HttpServlet {

    private final GoodsHistoryService goodsHistoryService = new GoodsHistoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "search";
        }

        if ("search".equals(action)) {
            handleSearch(request, response);
        } else if ("detail".equals(action)) {
            handleDetail(request, response);
        }

    }

    private void handleDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get id need detail
        Long id = Long.parseLong(request.getParameter("id"));

        // call service
        ImportHistoryDTO history = goodsHistoryService.getImportHistoryById(id);
        List<ImportHistoryDetailDTO> details = goodsHistoryService.getImportHistoryItems(id);

        request.setAttribute("history", history);
        request.setAttribute("details", details);
        request.getRequestDispatcher("/WEB-INF/inventory/import-history-detail.jsp").forward(request, response);

    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
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

        // get code
        String receiptCode = request.getParameter("receiptCode");

        // get date
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");

        // get data through service
        List<ImportHistoryDTO> importHistories = goodsHistoryService.getImportHistory(receiptCode, fromDateStr, toDateStr,
                pageNo);

        // count total records
        int totalRecords = goodsHistoryService.countImportHistory(receiptCode, fromDateStr, toDateStr);

        // calc total pages
        int totalPages = (int) Math.ceil((double) totalRecords / AppConstants.PAGE_SIZE);

        // set data
        request.setAttribute("receiptCode", receiptCode);
        request.setAttribute("importHistories", importHistories);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("fromDate", fromDateStr);
        request.setAttribute("toDate", toDateStr);

        // forward to jsp
        request.getRequestDispatcher("/WEB-INF/inventory/import-history.jsp").forward(request, response);
    }
}
