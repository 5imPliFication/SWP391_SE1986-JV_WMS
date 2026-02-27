package com.example.service;

import com.example.dao.GoodsHistoryDAO;
import com.example.dto.ImportHistoryDTO;
import com.example.dto.ImportHistoryDetailDTO;

import java.util.List;

public class GoodsHistoryService {
    private final GoodsHistoryDAO goodsHistoryDAO = new GoodsHistoryDAO();

    public List<ImportHistoryDTO> getImportHistory(String code, String fromDate, String toDate, int pageNo) {
        return goodsHistoryDAO.getImportHistory(code, fromDate, toDate, pageNo);
    }

    public int countImportHistory(String code, String fromDate, String toDate) {
        return goodsHistoryDAO.countImportHistory(code, fromDate, toDate);
    }

    public ImportHistoryDTO getImportHistoryById(Long id) {
        return goodsHistoryDAO.getImportHistoryById(id);
    }

    public List<ImportHistoryDetailDTO> getImportHistoryItems(Long id) {
        return goodsHistoryDAO.getImportHistoryItems(id);
    }
}
