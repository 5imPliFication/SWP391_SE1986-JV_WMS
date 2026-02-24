package com.example.service;

import com.example.dao.InventoryDAO;
import com.example.dto.ExportOrderDTO;
import com.example.dto.ImportProductItemDTO;
import com.example.dto.ProductDTO;
import com.example.model.Product;
import com.example.model.ProductItem;
import com.example.validator.ImportProductItemValidator;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {

    // init productDAO
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    // search product by name
    public List<ProductDTO> findProductByName(String name) {
        return inventoryDAO.findProductByName(name);
    }

    // save list product items
    public String saveProductItems(String[] serials, String[] prices, String[] productIds) {

        // init list product items
        List<ProductItem> productItems = new ArrayList<>();
        try {
            // loop
            for (int i = 0; i < serials.length; i++) {
                // get serial and price
                String serial = serials[i];
                double price = Double.parseDouble(prices[i]);

                // validator
                String messageValidator = ImportProductItemValidator.validateImportProductItem(serial, price);
                if (messageValidator != null) {
                    return messageValidator;
                }

                // check is exist serial
                if (inventoryDAO.isExistSerial(serial)) {
                    return "Serial " + serial + " already exists";
                }

                Long productId = Long.parseLong(productIds[i]);
                productItems.add(new ProductItem(serial, price, LocalDateTime.now(), productId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return inventoryDAO.saveProductItems(productItems) ? null : "Save successfully";
    }

    // save list products items by Excel
    public List<ImportProductItemDTO> readProductItemsFromExcel(Part filePart) {
        List<ImportProductItemDTO> importItems = new ArrayList<>();
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
                    List<ProductDTO> products = inventoryDAO.findProductByName(productName);
                    if (products == null || products.isEmpty()) {
                        // if not exist product -> skip
                        continue;
                    }

                    // get first product in list
                    ProductDTO product = products.get(0);
                    Long productId = product.getId();

                    // get serial
                    String serial = serialCell.getStringCellValue().trim();

                    // get price
                    double importPrice = priceCell.getNumericCellValue();

                    // init dto
                    ImportProductItemDTO dto = new ImportProductItemDTO();
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
    public Map<String, Object> getExportOrders(String fromDateStr, String toDateStr, int page, int pageSize) {
        LocalDate fromDate;
        LocalDate toDate;
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

        int offset = (page - 1) * pageSize;
        List<ExportOrderDTO> orders = inventoryDAO.searchExportOrders(fromDate, toDate, offset, pageSize);
        int totalOrders = inventoryDAO.countExportOrders(fromDate, toDate);
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

        // create map
        Map<String, Object> result = new HashMap<>();

        // set value for map
        result.put("orders", orders);
        result.put("totalPages", totalPages);
        return result;
    }
}
