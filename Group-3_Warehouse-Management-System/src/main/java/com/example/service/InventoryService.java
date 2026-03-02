package com.example.service;

import com.example.dao.InventoryDAO;
import com.example.dto.OrderDTO;
import com.example.dto.ProductItemDTO;
import com.example.dto.ProductDTO;
import com.example.util.AppConstants;
import com.example.validator.ImportProductItemValidator;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {

    // init productDAO
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    public String importProductItems(Long purchaseRequestId, Long warehouseUserId,
                                     String[] productIds, String[] serials, String[] prices) {

        if (productIds == null || serials == null || prices == null) {
            return "Invalid import data";
        }

        // get information from list import product item
        List<ProductItemDTO> importProductItemDTOs = getImportProductItemDTOs(productIds, serials, prices);

        String validationMessage = validateProductItems(importProductItemDTOs);
        if (validationMessage != null) {
            return validationMessage;
        }

        return inventoryDAO.saveProductItems(purchaseRequestId, warehouseUserId, importProductItemDTOs)
                ? null
                : "Import failed";
    }

    // get list product items import from table import 
    private List<ProductItemDTO> getImportProductItemDTOs(String[] productIds, String[] serials, String[] prices) {

        // init list to store
        List<ProductItemDTO> items = new ArrayList<>();

        // loop any row
        for (int i = 0; i < serials.length; i++) {
            String serial = serials[i];
            double price = Double.parseDouble(prices[i]);
            Long productId = Long.parseLong(productIds[i]);

            ProductItemDTO dto = new ProductItemDTO();
            dto.setProductId(productId);
            dto.setSerial(serial);
            dto.setImportPrice(price);

            items.add(dto);
        }
        return items;
    }

    // check valid data from list product items import
    private String validateProductItems(List<ProductItemDTO> productItemDTOs) {
        for (ProductItemDTO item : productItemDTOs) {
            String serial = item.getSerial();
            double price = item.getImportPrice();

            // validator
            String messageValidator = ImportProductItemValidator.validateImportProductItem(serial, price);
            if (messageValidator != null) {
                return messageValidator;
            }

            // check is exist serial
            if (inventoryDAO.isExistSerial(serial)) {
                return "Serial " + serial + " already exists";
            }
        }
        return null;
    }

    // get list products items by Excel
    public List<ProductItemDTO> readProductItemsFromExcel(Part filePart) {
        List<ProductItemDTO> importItems = new ArrayList<>();
        // if not have file upload -> return
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        } else {
            try (InputStream inputStream = filePart.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                // get first sheet
                Sheet sheet = workbook.getSheetAt(0);

                // mark header
                boolean isFirstRow = true;

                // loop any row in sheet
                for (Row row : sheet) {
                    // skip header row
                    if (isFirstRow) {
                        isFirstRow = false;
                        continue;
                    }

                    // read cell in row
                    Cell nameCell = row.getCell(0);
                    Cell serialCell = row.getCell(1);
                    Cell priceCell = row.getCell(2);

                    // check data if null -> skip row
                    if (nameCell == null || serialCell == null || priceCell == null) {
                        continue;
                    }

                    // get name of product
                    String productName = nameCell.getStringCellValue().trim();

                    // get product id by name
                    Long productId = inventoryDAO.findProductIdByName(productName);
                    if (productId == null) {
                        // if not exist product -> skip
                        continue;
                    }

                    // get serial
                    String serial = serialCell.getStringCellValue().trim();

                    // get price of import product item
                    double importPrice = priceCell.getNumericCellValue();

                    // init dto
                    ProductItemDTO dto = new ProductItemDTO();
                    dto.setProductId(productId);
                    dto.setProductName(productName);
                    dto.setSerial(serial);
                    dto.setImportPrice(importPrice);

                    // add to list
                    importItems.add(dto);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return importItems;
    }

    // handle export product items
    public Map<String, Object> getExportOrders(String name, String fromDateStr, String toDateStr, String status, int pageNo) {
        LocalDate fromDate;
        LocalDate toDate;

        // parse into format date
        if (fromDateStr != null && !fromDateStr.isEmpty()) {
            fromDate = LocalDate.parse(fromDateStr);
        } else {
            fromDate = null;
        }

        if (toDateStr != null && !toDateStr.isEmpty()) {
            toDate = LocalDate.parse(toDateStr);
        } else {
            toDate = null;
        }

        int offset = (pageNo - 1) * AppConstants.PAGE_SIZE;
        List<OrderDTO> orders = inventoryDAO.searchExportOrders(name, fromDate, toDate, status, offset);

        // handle pagination
        int totalOrders = getTotalOrders(name, fromDate, toDate, status);
        int totalPages = (int) Math.ceil((double) totalOrders / AppConstants.PAGE_SIZE);

        // create map
        Map<String, Object> result = new HashMap<>();

        // set value for map
        result.put("orders", orders);
        result.put("totalPages", totalPages);
        return result;
    }


    public int getTotalOrders(String name, LocalDate fromDate, LocalDate toDate, String status) {
        return inventoryDAO.countExportOrders(name, fromDate, toDate, status);
    }
}
