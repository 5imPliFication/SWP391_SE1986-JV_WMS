package com.example.controller.inventory;

import com.example.dto.GoodsReceiptDTO;
import com.example.dto.GoodsReceiptItemDTO;
import com.example.service.GoodsHistoryService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/inventory/import/history")
public class ImportHistoryServlet extends HttpServlet {

    private final GoodsHistoryService goodsHistoryService = new GoodsHistoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
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

        // get id of goods receipt need detail
        Long id = Long.parseLong(request.getParameter("id"));

        // call service
        GoodsReceiptDTO goodsReceipt = goodsHistoryService.getGoodsReceiptById(id);

        System.out.println(goodsReceipt);
        List<GoodsReceiptItemDTO> goodsReceiptItems = goodsHistoryService.getGoodsReceiptItems(id);

        for (GoodsReceiptItemDTO goodsReceiptItem : goodsReceiptItems) {
            System.out.println(goodsReceiptItem);
        }
        request.setAttribute("goodsReceipt", goodsReceipt);
        request.setAttribute("goodsReceiptItems", goodsReceiptItems);
        request.getRequestDispatcher("/WEB-INF/inventory/import-history-detail.jsp").forward(request, response);

    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get pageNo
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = AppConstants.DEFAULT_PAGE_NO;
        if (pageNoStr != null && !pageNoStr.isEmpty()) {
            try {
                pageNo = Integer.parseInt(pageNoStr);
            } catch (NumberFormatException e) {
                pageNo = AppConstants.DEFAULT_PAGE_NO;
            }
        }

        // get code
        String receiptCode = request.getParameter("receiptCode");

        // get date
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");

        // call service
        List<GoodsReceiptDTO> goodsReceipts = goodsHistoryService.getGoodsReceipts(receiptCode, fromDateStr, toDateStr,
                pageNo);

        // count total records
        int totalRecords = goodsHistoryService.countGoodsReceipts(receiptCode, fromDateStr, toDateStr);

        // calc total pages
        int totalPages = (int) Math.ceil((double) totalRecords / AppConstants.PAGE_SIZE);

        // set data
        request.setAttribute("receiptCode", receiptCode);
        request.setAttribute("goodsReceipts", goodsReceipts);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("fromDate", fromDateStr);
        request.setAttribute("toDate", toDateStr);

        // forward to jsp
        request.getRequestDispatcher("/WEB-INF/inventory/import-history.jsp").forward(request, response);
    }
}
