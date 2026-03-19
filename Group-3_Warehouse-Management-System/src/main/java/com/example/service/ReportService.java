package com.example.service;

import com.example.dao.ReportDAO;
import com.example.dto.ReportItemDTO;

import java.util.List;

public class ReportService {

    private final ReportDAO reportDAO = new ReportDAO();

    public List<Long> getImportChartDataByYear(int year) {
        return reportDAO.getImportChartDataByYear(year);
    }

    public List<ReportItemDTO> getItems(int month, int year) {
        // validate month
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        return reportDAO.getImportItems(month, year);
    }

    public List<Long> getInventoryChartDataByYear(int year) {
        return reportDAO.getInventoryChartDataByYear(year);
    }

    public List<ReportItemDTO> getInventoryItems(int month, int year) {
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return reportDAO.getInventoryItems(month, year);
    }

}
