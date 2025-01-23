package org.srivi.Trading.QE;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {
    public static Map<String, String[]> readAccountData(InputStream inputStream) throws IOException {
        Map<String, String[]> accounts = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                // Read cell values with null checks
                String accountSet = getCellValue(row.getCell(0));
                String username = getCellValue(row.getCell(1));
                String password = getCellValue(row.getCell(2));
                String holder = getCellValue(row.getCell(3));
                String type = getCellValue(row.getCell(4));
                String transfer = getCellValue(row.getCell(5));
                String offers = getCellValue(row.getCell(6));
                String paymentPlan = getCellValue(row.getCell(7));
                String creditLimit = getCellValue(row.getCell(8));

                // Store in map with account set as key
                accounts.put(accountSet, new String[]{username, password, holder, type, transfer, offers, paymentPlan, creditLimit});
            }
        }

        return accounts;
    }

    private static String getCellValue(Cell cell) {
        return cell == null ? "" : cell.getStringCellValue();
    }
}