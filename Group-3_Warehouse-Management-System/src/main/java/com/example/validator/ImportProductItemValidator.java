package com.example.validator;

public class ImportProductItemValidator {

    // check validate input of import product item
    public static String validateImportProductItem(String serial, Double price){

        // check serial
        if(serial == null || serial.trim().isEmpty()){
            return "Invalid serial number";
        }

        // check price
        if(price == null || price <= 0){
            return "Invalid price";
        }
        return null;
    }
}
