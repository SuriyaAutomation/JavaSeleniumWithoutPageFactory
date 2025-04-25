package org.google.home.utils;

import java.io.*;
import java.util.*;

public class CSVUtils {

    private static final String FILE_PATH = "output/ConfigData.csv";
    private static final List<String> HEADERS = Arrays.asList("lane type", "config name", "total price", "value", "amount");

    
       
    // -------- syntax to add all data which ever i wanna feed -----------------------
    
    public static void inputCSV(String... headerValuePairs) throws IOException {
        if (headerValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Each header must have a corresponding value.");
        }

        Map<String, String> rowData = new LinkedHashMap<>();
        for (int i = 0; i < headerValuePairs.length; i += 2) {
            String header = headerValuePairs[i].trim().toLowerCase();
            String value = headerValuePairs[i + 1];
            rowData.put(header, value);
        }

        File file = new File(FILE_PATH);
        boolean fileExists = file.exists();
        List<String[]> allRows = new ArrayList<>();

        // Read existing rows (if any)
        if (fileExists) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                    allRows.add(line.split(",", -1));
                }
            }
        }

        // Add header if missing
        if (allRows.isEmpty() || !Arrays.equals(
                Arrays.stream(allRows.get(0)).map(String::toLowerCase).toArray(),
                HEADERS.toArray()
        )) {
            allRows.clear();
            allRows.add(HEADERS.toArray(new String[0]));
        }

        // Create new row
        String[] newRow = new String[HEADERS.size()];
        for (int i = 0; i < HEADERS.size(); i++) {
            String header = HEADERS.get(i).trim().toLowerCase();
            newRow[i] = rowData.getOrDefault(header, "");
        }

        allRows.add(newRow);

        // Ensure parent directory exists
        file.getParentFile().mkdirs();

        // Write all rows back to CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String[] row : allRows) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    }
    
    
//    how to use above 
    
    public class Main {
        public static void main(String[] args) throws IOException {
			CSVUtils.inputCSV("config name", "abc123", "lane type", "Lane 1", "total price", "$500", "value", "45", "amount", "750");
			CSVUtils.inputCSV("config name", "abc123", "lane type", "Lane 2", "total price", "$500", "value", "45", "amount", "750");
			CSVUtils.inputCSV("config name", "abc123", "lane type", "Lane 3", "total price", "$500", "value", "45", "amount", "750");
			CSVUtils.inputCSV("config name", "abc123", "lane type", "Lane 1", "total price", "$500", "value", "45", "amount", "750");
        }
    }

    // output will be 
    
//    lane type,config name,total price,value,amount
//    Lane 1,abc123,$500,45,750
//    Lane 2,abc123,$500,45,750
//    Lane 3,abc123,$500,45,750
//    Lane 1,abc123,$500,45,750

    
    
    // ---------------------- getting data and deleting those data after that ---------------------------------------------
    
    
    public static List<List<String>> searchAndHandleRow(String header, String value, String action) throws IOException {
        
    	File file = new File(FILE_PATH);
        List<List<String>> matchedRows = new ArrayList<>();

        if (!file.exists()) {
            System.out.println("CSV file not found.");
            return matchedRows;
        }

        List<String[]> allRows = new ArrayList<>();

        // Read CSV into allRows
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                allRows.add(line.split(",", -1));
            }
        }

        if (allRows.isEmpty()) return matchedRows;

        String[] headers = allRows.get(0);
        int headerIndex = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(header.trim())) {
                headerIndex = i;
                break;
            }
        }

        if (headerIndex == -1) {
            System.out.println("Header not found.");
            return matchedRows;
        }

        List<String[]> updatedRows = new ArrayList<>();
        updatedRows.add(headers); // Keep header row

        for (int i = 1; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row.length <= headerIndex) continue;

            if (row[headerIndex].trim().equalsIgnoreCase(value.trim())) {
                matchedRows.add(Arrays.asList(row));

                if (!"delete".equalsIgnoreCase(action)) {
                    updatedRows.add(row); // keep it if not deleting
                }
            } else {
                updatedRows.add(row); // row doesn't match, so keep it
            }
        }

        // If action is delete, write back only updatedRows
        if ("delete".equalsIgnoreCase(action)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String[] row : updatedRows) {
                    writer.write(String.join(",", row));
                    writer.newLine();
                }
            }
        }

        return matchedRows;
    }

    
    ///------------------ Example of how to handle these getting and deleting existing data using methods -----------------------------
    
  //-------- sample csv file -----------------------
//  lane type,config name,total price,value,amount
//  Lane 1,lkjsd7657lb,$785985,123,789
//  Lane 2,HJG-9009,$559988,200,400
    
    
    
        public static void main(String[] args) throws IOException {
            // ✅ Fetch rows with lane type = Lane 1 and keep them (just print)
            List<List<String>> results = CSVUtils.searchAndHandleRow("lane type", "Lane 1", "keep");
            for (List<String> row : results) {
                System.out.println(row);
            }
            
            //  ✅ return value -- Lane 1,lkjsd7657lb,$785985,123,789

            // ✅ Fetch and delete rows with config name = "HJG-9009"
            CSVUtils.searchAndHandleRow("config name", "HJG-9009", "delete");
        }
    }

    
    // if we call ----------------
    
//    CSVUtils.searchAndHandleRow("config name", "HJG-9009", "delete");
    
    // csv becomes ------------------
    
//    lane type,config name,total price,value,amount
//    Lane 1,lkjsd7657lb,$785985,123,789


    

