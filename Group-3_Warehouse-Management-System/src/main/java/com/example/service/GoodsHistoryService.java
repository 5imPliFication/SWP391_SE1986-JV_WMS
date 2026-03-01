package com.example.service;

import com.example.dao.GoodsHistoryDAO;
import com.example.dto.GoodsReceiptDTO;
import com.example.dto.GoodsReceiptItemDTO;
import com.example.dto.ImportHistoryDTO;

import java.time.LocalDate;
import java.util.List;

public class GoodsHistoryService {
    private final GoodsHistoryDAO goodsHistoryDAO = new GoodsHistoryDAO();


    public List<GoodsReceiptDTO> getGoodsReceipts(String code, String fromDateStr, String toDateStr, int pageNo) {
        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (fromDateStr != null && !fromDateStr.isEmpty()) {
            fromDate = LocalDate.parse(fromDateStr);
        }
        if (toDateStr != null && !toDateStr.isEmpty()) {
            toDate = LocalDate.parse(toDateStr);
        }

        return goodsHistoryDAO.getGoodsReceipts(code, fromDate, toDate, pageNo);
    }

    public int countGoodsReceipts(String code, String fromDateStr, String toDateStr) {
        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (fromDateStr != null && !fromDateStr.isEmpty()) {
            fromDate = LocalDate.parse(fromDateStr);
        }
        if (toDateStr != null && !toDateStr.isEmpty()) {
            toDate = LocalDate.parse(toDateStr);
        }

        return goodsHistoryDAO.countImportHistory(code, fromDate, toDate);
    }

    public GoodsReceiptDTO getGoodsReceiptById(Long id) {
        return goodsHistoryDAO.getGoodsReceiptById(id);
    }

    public List<GoodsReceiptItemDTO> getGoodsReceiptItems(Long id) {
        return goodsHistoryDAO.getGoodsReceiptItems(id);
    }
}
