package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ExportToExcel extends Main {
    String filepath;

    public void exportData(String filepath) {
        this.filepath = filepath;
        String query = "SELECT * FROM " + table;
        try (PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("task 9");
            Row row = sheet.createRow(0);
            for (int i = 0; i < columnCount; i++) {
                row.createCell(i).setCellValue(rs.getMetaData().getColumnName(i+1));
            }

            int rowIndex = 1;
            while (rs.next()) {
                Row row1 = sheet.createRow(rowIndex++);
                row1.createCell(0).setCellValue(rs.getInt(1));
                for (int i = 1; i < columnCount; i++) {
                    if (rs.getArray(i+1) == null) {
                        row1.createCell(i).setCellValue("null");
                    } else {
                        row1.createCell(i).setCellValue(rs.getArray(i+1).toString());
                    }
                }
            }

            columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }
            try (FileOutputStream fos = new FileOutputStream(filepath)) {
                wb.write(fos);
            } catch (IOException e) {
                System.out.println("Ошибка при записи Excel-файла: " + e);
            } finally {
                wb.close();
                System.out.println("Данные успешно сохранены в Excel-файл: " + filepath);
            }
        } catch (IOException | SQLException e) {
            System.out.println("Ошибка при экспорте данных: " + e);
        }
    }

    public void printExcelData(String filepath) {
        try (Workbook wb = new XSSFWorkbook(filepath)) {
            Sheet sheet = wb.getSheetAt(0);
            System.out.println("\nДанные из Excel:");


            StringBuilder border = new StringBuilder("+");

            Row names = sheet.getRow(0);

            int[] maxLength = new int[names.getLastCellNum()];

            for (Cell name : names) {
                maxLength[name.getColumnIndex()] = name.toString().length();

            }
            for (Row row : sheet) {
                for (int i = 0; i < maxLength.length; i++) {
                    Cell obj = row.getCell(i);
                    int length = obj.toString().length();
                    if (length > maxLength[i]) {
                        maxLength[i] = length;
                    }
                }
            }

            for (int width : maxLength) {
                border.append("-".repeat(width + 2)).append("+");
            }

            System.out.println(border);

            for (Row row : sheet) {
                StringBuilder rowStr = new StringBuilder("|");
                for (int i = 0; i < maxLength.length; i++) {
                    Cell obj = row.getCell(i);
                    rowStr.append(" ").append(String.format("%-" + maxLength[i] + "s", obj)).append(" |");
                }
                System.out.println(rowStr);
                System.out.println(border);
            }

        } catch (IOException e) {
            System.out.println("Ошибка при чтении Excel-файла: " + e.getMessage());
        }
    }
}
