package org.google.home.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.google.home.config.factory.ConfigFactory;

public class GenericHelper {

	public static String filePath = System.getProperty("user.dir")+ ConfigFactory.getConfig().defaultDownloadingDirectory().trim();
	public static String finalDateObj;

	public static void deleteFile(String file) {

		File directory = new File(filePath);
		for (File f : directory.listFiles()) {
			if (f.getName().startsWith(file)) {
				f.delete();
			}
		}
	}
		    	
	public static PDDocumentInformation extractMetaDataFromPdf(String fileName) {
		PDDocumentInformation pdd = null;
		File directory = new File(filePath);
		try {
			for (File f : directory.listFiles()) {
				if (f.getName().startsWith(fileName)) {
					PDDocument document = PDDocument.load(new File(f.toString()));		            
		            pdd = document.getDocumentInformation();
		            document.close();
				}
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}		
		return pdd;
	}

	public static boolean waitForFileToDownload(String fileName) {
		boolean fileDownloaded = false;

		File file = new File(filePath + "/" + fileName);
		int timeout = ConfigFactory.getConfig().explicittimeout(); // Set a timeout (in seconds)
		int timeSpent = 0;

		while (!file.exists() && timeSpent < timeout) {
			try {
				TimeUnit.SECONDS.sleep(1); // Wait for 1 second
				timeSpent++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (file.exists()) {
			System.out.println("File downloaded: " + file.getAbsolutePath());
			fileDownloaded = true;
		}
		return fileDownloaded;
	}
 
	public static void writeExcelWorkBook(String fileName, String dataSet) {

		try (FileInputStream fis = new FileInputStream(filePath + "/" + fileName);
				Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
			// Check if the header is set correctly
			Row headerRow = sheet.getRow(0);
			if (!isHeaderCorrect(headerRow)) {
				System.err.println("Header is not correct. Aborting.");
				return;
			}

			// Parse the dataset and write to the sheet
			String[] data = dataSet.split("\\|");
			for (int i = 0; i < data.length; i++) {
				String[] cell = data[i].split(",");
				Row newRow = sheet.createRow(i + 1); // Start inserting data from row 1
				for (int k = 0; k < cell.length; k++) {
					// Try to determine the type of value					
					newRow.createCell(k).setCellValue(cell[k]); // For text values					
				}
			}

			// Write changes back to the file
			try (FileOutputStream fos = new FileOutputStream(filePath + "/" + fileName)) {
				workbook.write(fos);
				System.out.println("Excel file updated successfully.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error processing the Excel file: " + e.getMessage());
		}
	}

	public static boolean isHeaderCorrect(Row headerRow) {
		String[] expectedHeaders = { "User ID or AID", "Amount", "Donation Method", "Frequency",
				"Transaction/Check Number", "Date" };

		for (int i = 0; i < expectedHeaders.length; i++) {
			Cell cell = headerRow.getCell(i);
			if (cell == null || !cell.getStringCellValue().equals(expectedHeaders[i])) {
				return false;
			}
		}
		return true;
	}

	public static Date convertStringToDate(String str) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(str);
		} catch (ParseException e) {
			throw new RuntimeException("Invalid date format: " + str);
		}
	}
	
	public static String getTodayDate() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        finalDateObj = dateObj.format(myFormatObj);
        return finalDateObj;
	}
	
	public static String getDate() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        finalDateObj = dateObj.format(myFormatObj);
        return finalDateObj;
	}
	
	public static boolean getFileName(String fileName) {
		boolean fileNameValid = false;

		File file = new File(filePath + "/" + fileName);
		String downloadedFileName = file.getName();
		if(downloadedFileName.equals(fileName)) {
			System.out.println("File downloaded successfully with the correct name.");
        } else {
            System.out.println("File download or name validation failed.");
        
		}
		return fileNameValid;
	}


}