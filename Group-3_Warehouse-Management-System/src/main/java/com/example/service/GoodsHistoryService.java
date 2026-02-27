package com.example.service;

import com.example.dao.GoodsHistoryDAO;
import com.example.dto.ImportHistoryDTO;
import com.example.dto.ImportHistoryDetailDTO;

import java.time.LocalDate;
import java.util.List;

public class GoodsHistoryService {
    private final GoodsHistoryDAO goodsHistoryDAO = new GoodsHistoryDAO();

    public List<ImportHistoryDTO> getImportHistory(String code, String fromDateStr, String toDateStr, int pageNo) {
        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (fromDateStr != null && !fromDateStr.isEmpty()) {
            fromDate = LocalDate.parse(fromDateStr);
        }
        if (toDateStr != null && !toDateStr.isEmpty()) {
            toDate = LocalDate.parse(toDateStr);
        }

        return goodsHistoryDAO.getImportHistory(code, fromDate, toDate, pageNo);
    }

    public int countImportHistory(String code, String fromDateStr, String toDateStr) {
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

    public ImportHistoryDTO getImportHistoryById(Long id) {
        return goodsHistoryDAO.getImportHistoryById(id);
    }

    public List<ImportHistoryDetailDTO> getImportHistoryItems(Long id) {
        return goodsHistoryDAO.getImportHistoryItems(id);
    }
}
