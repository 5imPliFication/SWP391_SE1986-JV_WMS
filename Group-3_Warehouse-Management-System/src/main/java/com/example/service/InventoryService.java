package com.example.service;

import com.example.dao.InventoryDAO;
import com.example.dao.StockMovementDAO;
import com.example.dto.ExportDTO;
import com.example.dto.ExportProductDTO;
import com.example.dto.OrderDTO;
import com.example.dto.ProductItemDTO;
import com.example.enums.MovementType;
import com.example.enums.ReferenceType;
import com.example.model.User;
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
import java.util.Set;
import java.util.HashSet;

public class InventoryService {

    // init productDAO
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    public String importProductItems(Long purchaseRequestId, Long warehouseUserId,
                                     String[] productIds, String[] serials, String[] prices, String supplier) {

        // get information from list import product item
        List<ProductItemDTO> importProductItemDTOs = getImportProductItemDTOs(productIds, serials, prices);

        String validationMessage = validateProductItems(importProductItemDTOs);
        if (validationMessage != null) {
            return validationMessage;
        }

        return inventoryDAO.saveProductItems(purchaseRequestId, warehouseUserId, importProductItemDTOs, supplier)
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
            String priceStr = prices[i];
            String productIdStr = productIds[i];

            double price = Double.parseDouble(priceStr);
            long productId = Long.parseLong(productIdStr);

            ProductItemDTO dto = new ProductItemDTO();
            dto.setProductId(productId);
            dto.setSerial(serial.trim());
            dto.setImportPrice(price);

            items.add(dto);
        }
        return items;
    }

    // check valid data from list product items import
    private String validateProductItems(List<ProductItemDTO> productItemDTOs) {
        Set<String> seenSerials = new HashSet<>();
        for (ProductItemDTO item : productItemDTOs) {
            String serial = item.getSerial();
            Double price = item.getImportPrice();

            // validator
            String messageValidator = ImportProductItemValidator.validateImportProductItem(serial, price);
            if (messageValidator != null) {
                return messageValidator;
            }

            // check duplicate in current list
            if (!seenSerials.add(serial)) {
                return "Duplicate serial " + serial + " in the input list";
            }

            // check is exist serial
            if (inventoryDAO.isExistSerial(serial)) {
                return "Serial " + serial + " already exists";
            }
        }
        return null;
    }

    public ExportDTO getExportOrder(Long orderId) {
        return inventoryDAO.getPendingOrder(orderId);
    }


    public void handleExport(User user, ExportDTO exportOrder, String[] serials) {
        if (serials == null || serials.length == 0) {
            throw new IllegalArgumentException("No serial numbers provided");
        }

        // Check for duplicates and empty serials
        Set<String> seenSerials = new HashSet<>();
        for (String serial : serials) {
            if (serial == null || serial.trim().isEmpty()) {
                throw new IllegalArgumentException("Serial number cannot be empty");
            }
            if (!seenSerials.add(serial.trim())) {
                throw new IllegalArgumentException("Duplicate serial number found: " + serial);
            }
        }

        int serialIndex = 0;
        // order item id -> list serials assigned to this item
        Map<Long, List<String>> orderItemSerialsMap = new HashMap<>();
        for (ExportProductDTO item : exportOrder.getItems()) {
            List<String> assignedSerials = new ArrayList<>();
            for (int i = 0; i < item.getQuantity(); i++) {
                String serial = serials[serialIndex++];
                assignedSerials.add(serial.trim());
            }
            // product id -> serial
            orderItemSerialsMap.put(item.getId(), assignedSerials);
        }

        // execute all export steps within a single transaction
        inventoryDAO.executeExport(exportOrder.getId(), user.getId(), orderItemSerialsMap);
    }
}
