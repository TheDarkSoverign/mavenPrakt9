package org.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExportToExcel extends Main {
    String filepath;

    public void exportData(String table, String filepath) {
        this.filepath = filepath;

        String printAll = "SELECT * FROM " + table;
        try (PreparedStatement pst = con.prepareStatement(printAll); ResultSet rs = pst.executeQuery()) {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("task 1");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue(rs.getMetaData().getColumnName(1));
            row.createCell(1).setCellValue(rs.getMetaData().getColumnName(2));
            row.createCell(2).setCellValue(rs.getMetaData().getColumnName(3));
            row.createCell(3).setCellValue(rs.getMetaData().getColumnName(4));
            row.createCell(4).setCellValue(rs.getMetaData().getColumnName(5));
            row.createCell(5).setCellValue(rs.getMetaData().getColumnName(6));
            row.createCell(6).setCellValue(rs.getMetaData().getColumnName(7));
            row.createCell(7).setCellValue(rs.getMetaData().getColumnName(8));
            row.createCell(8).setCellValue(rs.getMetaData().getColumnName(9));

            int rowIndex = 1;
            while (rs.next()) {
                Row row1 = sheet.createRow(rowIndex++);
                row1.createCell(0).setCellValue(rs.getInt(1));
                row1.createCell(1).setCellValue(rs.getInt(2));
                row1.createCell(2).setCellValue(rs.getInt(3));
                row1.createCell(3).setCellValue(rs.getInt(4));
                row1.createCell(4).setCellValue(rs.getInt(5));
                row1.createCell(5).setCellValue(rs.getInt(6));
                row1.createCell(6).setCellValue(rs.getInt(7));
                row1.createCell(7).setCellValue(rs.getInt(8));
                row1.createCell(8).setCellValue(rs.getInt(9));

            }
            int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
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
}
