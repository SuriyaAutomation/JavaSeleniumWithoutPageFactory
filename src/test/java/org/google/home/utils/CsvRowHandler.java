package org.google.home.utils;

import java.io.*;
import java.util.*;

public class CsvRowHandler {

    private static final String FILE_PATH = "your_csv_file_path_here.csv";

    // Existing method (simple search by one header)
    public static List<List<String>> searchAndHandleRow(String header, String value, String action) throws IOException {
        return searchAndHandleRow(header, value, action, null);
    }

    // Overloaded method (primary + secondary filtering)
    public static List<List<String>> searchAndHandleRow(String header, String value, String action, Map<String, String> additionalFilters) throws IOException {
        File file = new File(FILE_PATH);
        List<List<String>> matchedRows = new ArrayList<>();

        if (!file.exists()) {
            System.out.println("CSV file not found.");
            return matchedRows;
        }

        List<String[]> allRows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                allRows.add(line.split(",", -1));
            }
        }

        if (allRows.isEmpty()) return matchedRows;

        String[] headers = allRows.get(0);
        Map<String, Integer> headerIndexMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerIndexMap.put(headers[i].trim().toLowerCase(), i);
        }

        if (!headerIndexMap.containsKey(header.trim().toLowerCase())) {
            System.out.println("Primary header not found.");
            return matchedRows;
        }

        int primaryIndex = headerIndexMap.get(header.trim().toLowerCase());

        List<String[]> updatedRows = new ArrayList<>();
        updatedRows.add(headers); // Keep header row

        for (int i = 1; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row.length <= primaryIndex) continue;

            boolean primaryMatch = row[primaryIndex].trim().equalsIgnoreCase(value.trim());
            boolean secondaryMatch = true;

            if (additionalFilters != null && !additionalFilters.isEmpty()) {
                for (Map.Entry<String, String> entry : additionalFilters.entrySet()) {
                    String filterHeader = entry.getKey();
                    String expectedValue = entry.getValue();

                    if (!headerIndexMap.containsKey(filterHeader.trim().toLowerCase())) {
                        secondaryMatch = false;
                        break;
                    }
                    int idx = headerIndexMap.get(filterHeader.trim().toLowerCase());

                    if (idx >= row.length || !row[idx].trim().equalsIgnoreCase(expectedValue.trim())) {
                        secondaryMatch = false;
                        break;
                    }
                }
            }

            if (primaryMatch && secondaryMatch) {
                matchedRows.add(Arrays.asList(row)); // Collect matched row

                // Do NOT add to updatedRows if action is delete
                if (!"delete".equalsIgnoreCase(action)) {
                    updatedRows.add(row);
                }
            } else {
                updatedRows.add(row); // Always keep non-matching rows
            }
        }

        // Write back updated content if deletion is required
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

    // Sample usage
    public static void main(String[] args) throws IOException {
        Map<String, String> additionalFilters = new HashMap<>();
        additionalFilters.put("Equipment", "Crane");
        additionalFilters.put("Region", "North");

        List<List<String>> results = searchAndHandleRow("LaneType", "Lane1", "delete", additionalFilters);

        for (List<String> row : results) {
            System.out.println("Deleted row: " + row);
        }
    }
}
