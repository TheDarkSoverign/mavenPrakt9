package org.example;

import java.sql.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class Main {
    protected static Scanner sc = new Scanner(System.in);
    protected static Connection con;
    protected static String table = "task1";

    static String Url = "jdbc:postgresql://localhost:5432/postgres";

    static {
        try {
            con = DriverManager.getConnection(Url, "postgres", "postgres");
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных: " + e.getMessage());
        }

        String query = "CREATE TABLE IF NOT EXISTS task1 (id SERIAL, sum INT, sub INT, mul INT, div INT, mod INT, abs_1 INT, abs_2 INT, pow INT)";
        try {
            Statement st = con.createStatement();
            st.executeUpdate(query);
            table = "task1";
            System.out.println("Используется таблица по умолчанию - " + table);
        } catch (SQLException e) {
            System.out.println("Не удалось использовать таблицу по умолчанию, " + e.getMessage());
        }
    }

    protected static void menu() {
        int x = 0;
        String s = "";
        ArrayPI tasks = new ArrayPI();
        ExportToExcel export = new ExportToExcel();
        while (!"0".equals(s)) {
            System.out.println("Меню программы:");
            System.out.println("1. Вывести все таблицы.");
            System.out.println("2. Создать/выбрать таблицу.");
            System.out.println("3. Ввести две матрицы (7х7) и сохранить в таблицу.");
            System.out.println("4. Перемножить матрицу и сохранить в таблицу.");
            System.out.println("5. Записать данные в Excel");
            System.out.println("0. Выход");
            System.out.print("Выберите пункт меню: ");
            s = sc.nextLine();
            try {
                x = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода");
            }
            switch (x) {
                case 1 -> tasks.task1();
                case 2 -> tasks.task2();
                case 3 -> tasks.task3();
                case 4 -> tasks.insertData();
                case 5 -> {
                    System.out.print("Введите название файла: ");
                    String filepath = sc.nextLine();

                    if (!filepath.contains(".xlsx")) {
                        filepath += ".xlsx";
                    }

                    export.exportData(table, filepath);
                }
                case 0 -> System.out.println("Пока!");
                default -> System.out.println("Неправильно выбран пункт меню! Попробуйте еще раз...");
            }
            x = 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("Подключились к БД. ");
        menu();
    }
}

class Task extends Main {



}