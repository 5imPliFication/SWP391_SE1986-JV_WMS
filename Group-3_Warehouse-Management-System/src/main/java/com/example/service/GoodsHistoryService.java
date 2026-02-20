package com.example.service;

import com.example.dao.GoodsHistoryDAO;
import com.example.dto.ImportHistoryDTO;
import java.util.List;

public class GoodsHistoryService {
    private final GoodsHistoryDAO goodsHistoryDAO = new GoodsHistoryDAO();

    public List<ImportHistoryDTO> getImportHistory(String fromDate, String toDate, int pageNo) {
        return goodsHistoryDAO.getImportHistory(fromDate, toDate, pageNo);
    }

    public int countImportHistory(String fromDate, String toDate) {
        return goodsHistoryDAO.countImportHistory(fromDate, toDate);
    }
}
