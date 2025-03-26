package org.google.home.utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateRangecalculator {

	


	    public static boolean isValidDateRange(String filePath, String fileName, String sheetName, String cellRef, int allowedMonths) {
	        File file = new File(filePath, fileName);

	        try (FileInputStream fis = new FileInputStream(file);
	             Workbook workbook = new XSSFWorkbook(fis)) {

	            Sheet sheet = workbook.getSheet(sheetName);
	            if (sheet == null) {
	                System.out.println("Sheet not found: " + sheetName);
	                return false;
	            }

	            // Convert cell reference B3 -> row index 2, column index 1
	            int rowIndex = 2;  // B3 corresponds to row index 2 (0-based)
	            int colIndex = 1;  // Column 'B' corresponds to index 1 (0-based)

	            Row row = sheet.getRow(rowIndex);
	            if (row == null) {
	                System.out.println("Row not found: " + (rowIndex + 1));
	                return false;
	            }

	            Cell cell = row.getCell(colIndex);
	            if (cell == null || cell.getCellType() != CellType.STRING) {
	                System.out.println("Invalid cell value at " + cellRef);
	                return false;
	            }

	            String dateRangeText = cell.getStringCellValue().trim();
	            System.out.println("Extracted Date Range: " + dateRangeText);

	            // Extract dates using regex
	            Pattern pattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})-(\\d{2}/\\d{2}/\\d{4})");
	            Matcher matcher = pattern.matcher(dateRangeText);

	            if (!matcher.matches()) {
	                System.out.println("Invalid date range format in cell " + cellRef);
	                return false;
	            }

	            // Parse extracted dates
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	            LocalDate startDate = LocalDate.parse(matcher.group(1), formatter);
	            LocalDate endDate = LocalDate.parse(matcher.group(2), formatter);

	            // Validate the date range
	            long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
	            System.out.println("Months Difference: " + monthsBetween);

	            return monthsBetween == allowedMonths;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public static void main(String[] args) {
	        String filePath = "C:/Users/YourUser/Documents/";
	        String fileName = "test.xlsx";
	        String sheetName = "Sheet1";
	        String cellRef = "B3";  // Cell containing the date range
	        int allowedMonths = 3;  // Change to 3, 6, or 12 for different validation periods

	        boolean isValid = isValidDateRange(filePath, fileName, sheetName, cellRef, allowedMonths);
	        System.out.println("Is date range valid? " + isValid);
	    }
	}


