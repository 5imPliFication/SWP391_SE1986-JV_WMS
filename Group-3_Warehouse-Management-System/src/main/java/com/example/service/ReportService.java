package com.example.service;

import com.example.dao.ReportDAO;
import com.example.dto.MonthSummaryDTO;
import com.example.dto.ReportItemDTO;
import com.example.enums.MovementType;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    private final ReportDAO reportDAO = new ReportDAO();

    public List<Long> getChartDataByYear(int year, MovementType movementType) {
        return reportDAO.getMovementChartDataByYear(year, movementType);
    }

    public List<ReportItemDTO> getItems(int month, int year, MovementType movementType) {
        // validate month
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        return reportDAO.getItems(month, year, movementType);
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


    public void validateQuarter(int quarter) {
        if (quarter < 1 || quarter > 4) {
            throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }
    }
}
