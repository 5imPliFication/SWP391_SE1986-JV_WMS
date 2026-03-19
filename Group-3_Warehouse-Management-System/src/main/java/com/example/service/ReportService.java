package com.example.service;

import com.example.dao.ReportDAO;
import com.example.dto.ReportItemDTO;
import com.example.enums.MovementType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    private final ReportDAO reportDAO = new ReportDAO();

    public List<Long> getImportChartDataByYear(int year) {
        return reportDAO.getImportChartDataByYear(year);
    }

    public Map<String, List<Long>> getExportAnnualOverviewByYear(int year) {
        List<Long> imported = reportDAO.getMovementChartDataByYear(year, MovementType.IMPORT);
        List<Long> exported = reportDAO.getMovementChartDataByYear(year, MovementType.EXPORT);
        long openingStock = reportDAO.getOpeningStockBeforeYear(year);

        List<Long> inStock = new ArrayList<>();
        long runningStock = openingStock;
        for (int i = 0; i < 12; i++) {
            runningStock += imported.get(i);
            runningStock -= exported.get(i);
            inStock.add(runningStock);
        }

        Map<String, List<Long>> overview = new LinkedHashMap<>();
        overview.put("inStock", inStock);
        overview.put("exported", exported);
        overview.put("imported", imported);
        return overview;
    }

    public Map<String, Object> getExportQuarterTrend(int year, int quarter) {
        validateQuarter(quarter);

        List<Long> exported = reportDAO.getMovementChartDataByYear(year, MovementType.EXPORT);
        int startMonthIndex = (quarter - 1) * 3;
        String[] monthLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            labels.add(monthLabels[startMonthIndex + i]);
            data.add(exported.get(startMonthIndex + i));
        }

        Map<String, Object> trend = new LinkedHashMap<>();
        trend.put("labels", labels);
        trend.put("data", data);
        return trend;
    }

    public List<ReportItemDTO> getItems(int month, int year) {
        // validate month
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        return reportDAO.getImportItems(month, year);
    }

    public void validateQuarter(int quarter) {
        if (quarter < 1 || quarter > 4) {
            throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }
    }
}
