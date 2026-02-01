package com.example.service;

import com.example.dao.ProductDAO;
import com.example.dto.ImportProductItemDTO;
import com.example.model.Product;
import com.example.model.ProductItem;
import com.example.validator.ImportProductItemValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    // init productDAO
    private final ProductDAO productDAO = new ProductDAO();

    // search product by name
    public List<Product> findProductByName(String name) {
        return productDAO.findProductByName(name);
    }

    // save list product items
    public boolean saveProductItems(String[] serials, String[] prices, String[] productIds) {

        // init list product items
        List<ProductItem> productItems = new ArrayList<>();
        try {
            // loop
            for (int i = 1; i <= serials.length; i++) {
                String serial = serials[i - 1];
                if (productDAO.isExistSerial(serial)) {
                    return false;
                }
                double price = Double.parseDouble(prices[i - 1]);
                if (price < 0) {
                    return false;
                }
                Long productId = Long.parseLong(productIds[i - 1]);
                productItems.add(new ProductItem(serial, price, LocalDateTime.now(), productId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return productDAO.saveProductItems(productItems);
    }

    // save list products items by Excel
    public List<ImportProductItemDTO> readProductItemsByExcel(Part filePart) {
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
                    List<Product> products = productDAO.findProductByName(productName);
                    if (products == null || products.isEmpty()) {
                        // if not exist product -> skip
                        continue;
                    }

                    // get first product in list
                    Product product = products.get(0);
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

    public Object saveProductItems(List<ImportProductItemDTO> importProductItemDTOS) {
        List<ProductItem> productItems = new ArrayList<>();
        for (ImportProductItemDTO importProductItemDTO : importProductItemDTOS) {
            String name = importProductItemDTO.getProductName();
            String serial = importProductItemDTO.getSerial();
            double importPrice = importProductItemDTO.getImportPrice();

            // validate data import
            String messageValidator = ImportProductItemValidator.validateImportProductItem(serial, importPrice);

            if (messageValidator != null) {
                return messageValidator;
            }
            // get serial and check exist
            if(productDAO.isExistSerial(serial)){
                return "Serial already exists";
            }


            productItems.add(new ProductItem(serial, importPrice, LocalDateTime.now(), Long.parseLong(name)));
        }
        return productDAO.saveProductItems(productItems);
    }
}
