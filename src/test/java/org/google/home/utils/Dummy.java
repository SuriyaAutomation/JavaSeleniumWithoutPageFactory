package org.google.home.utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Dummy {

	
	    public static boolean isHeaderCorrect(String filePath, String fileName, String sheetName, int rowNum, String[] expectedHeaders) {
	        File file = new File(filePath, fileName);
	        
	        try (FileInputStream fis = new FileInputStream(file);
	             Workbook workbook = new XSSFWorkbook(fis)) {
	            
	            Sheet sheet = workbook.getSheet(sheetName);
	            if (sheet == null) {
	                System.out.println("Sheet not found: " + sheetName);
	                return false;
	            }

	            Row headerRow = sheet.getRow(rowNum);
	            if (headerRow == null) {
	                System.out.println("Row not found: " + rowNum);
	                return false;
	            }

	            for (int i = 0; i < expectedHeaders.length; i++) {
	                Cell cell = headerRow.getCell(i);
	                if (cell == null || !cell.getStringCellValue().trim().equalsIgnoreCase(expectedHeaders[i].trim())) {
	                    System.out.println("Header mismatch at column " + i + ": Expected '" + expectedHeaders[i] + "', but found '" +
	                            (cell == null ? "null" : cell.getStringCellValue()) + "'");
	                    return false;
	                }
	            }

	            return true;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public static void main(String[] args) {
	        String filePath = "C:/Users/YourUser/Documents/";
	        String fileName = "test.xlsx";
	        String sheetName = "Sheet1";
	        int rowNum = 0; // Typically, headers are in the first row (index 0)
	        String[] expectedHeaders = {"Name", "Age", "Email", "Address"};

	        boolean result = isHeaderCorrect(filePath, fileName, sheetName, rowNum, expectedHeaders);
	        System.out.println("Is header correct? " + result);
	    
	}
}


