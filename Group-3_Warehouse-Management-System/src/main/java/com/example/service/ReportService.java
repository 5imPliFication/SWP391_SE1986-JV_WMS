package com.example.service;

import com.example.dao.ReportDAO;
import com.example.dto.MonthSummaryDTO;
import com.example.dto.ReportItemDTO;

import java.util.ArrayList;
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

    public List<MonthSummaryDTO> getMonthSummary(int year) {
        List<MonthSummaryDTO> list = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            long importQuantity = reportDAO.getImportByMonth(month, year);
            long exportQuantity = reportDAO.getExportByMonth(month, year);

            list.add(new MonthSummaryDTO(month, importQuantity, exportQuantity));
        }
        return list;
    }

}
