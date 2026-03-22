package com.example.service;

import com.example.dao.ReportDAO;
import com.example.dto.ExportProductOrderDTO;
import com.example.dto.InventoryMovementRowDTO;
import com.example.dto.MonthSummaryDTO;
import com.example.dto.ReportItemDTO;
import com.example.enums.MovementType;

import java.time.LocalDate;
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

    public List<Long> getInventoryChartDataByYear(int year) {
        return reportDAO.getInventoryChartDataByYear(year);
    }

    public List<Long> getExportChartDataByYear(int year) {
        return reportDAO.getMovementChartDataByYear(year, MovementType.EXPORT);
    }

    public List<ReportItemDTO> getExportItems(int month, int year) {
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return reportDAO.getExportItems(month, year);
    }

    public List<ExportProductOrderDTO> getExportOrdersByProduct(Long productId, int month, int year) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product is required");
        }
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return reportDAO.getExportOrdersByProduct(productId, month, year);
    }

    public int countExportOrdersByProduct(Long productId, int month, int year) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product is required");
        }
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return reportDAO.countExportOrdersByProduct(productId, month, year);
    }

    public List<ExportProductOrderDTO> getExportOrdersByProduct(Long productId, int month, int year, int pageNo, int pageSize) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product is required");
        }
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        if (pageNo < 1) {
            throw new IllegalArgumentException("Page number must be at least 1");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be positive");
        }

        int offset = (pageNo - 1) * pageSize;
        return reportDAO.getExportOrdersByProduct(productId, month, year, offset, pageSize);
    }

    public List<ReportItemDTO> getInventoryItems(int month, int year) {
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return reportDAO.getInventoryItems(month, year);
    }

    public List<InventoryMovementRowDTO> getInventoryMovementRows(int month, int year) {
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return reportDAO.getInventoryMovementRows(month, year);
    }

    public List<InventoryMovementRowDTO> getInventoryMovementRows(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("From date and to date are required");
        }
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date must be before or equal to to date");
        }
        return reportDAO.getInventoryMovementRows(fromDate, toDate);
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
