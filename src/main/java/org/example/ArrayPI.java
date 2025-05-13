package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayPI extends Main {
    int[][] matrix1;
    int[][] matrix2;

    public void task1() {
        String query = "SELECT table_name AS Названия_таблиц FROM information_schema.tables WHERE table_schema = '" + schema + "'";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            try {
                int nameLength = 15;
                while (rs.next()) {
                    int currentNameLength = rs.getString(1).length();
                    if (currentNameLength > nameLength) {
                        nameLength = currentNameLength;
                    }
                }
                String tablePart = "+" + "-".repeat(5) + "+" + "-".repeat(nameLength + 2) + "+";
                System.out.println("Список таблиц:");
                System.out.println(tablePart);
                System.out.printf("| %-3s | %-" + nameLength + "s |\n", "ID", "Названия таблиц");
                rs = st.executeQuery(query);
                int i = 1;
                while (rs.next()) {
                    String tableName = rs.getString("Названия_таблиц");
                    System.out.println(tablePart);
                    System.out.printf("| %-3d | %-" + nameLength + "s |\n", i++, tableName);
                }
                System.out.println(tablePart);
            } catch (SQLException e) {
                System.out.println("Не удалось вывести результат, " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
        }
    }

    public void task2() {
        System.out.print("Введите название таблицы: ");
        table = sc.next();
        sc.nextLine();
        String query = "CREATE TABLE IF NOT EXISTS " + table + " (ID SERIAL, matrix1 int[], matrix2 int[], matrixMult int[])";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.executeUpdate();
            System.out.println("Таблица " + table + " успешно создана/выбрана!");
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
            task2();
        }
    }

    public void task3() {
        System.out.println("Введите матрицу 1");
        matrix1 = createMatrix();
        System.out.println("Матрица 1: ");
        for (int[] i : matrix1) {
            System.out.println(Arrays.toString(i));
        }

        System.out.println("Введите матрицу 2");
        matrix2 = createMatrix();
        System.out.println("Матрица 2:");
        for (int[] i : matrix2) {
            System.out.println(Arrays.toString(i));
        }

        insertData();
        selectData();
    }

    public void task4() {
        if (matrix1 == null || matrix2 == null) {
            System.out.println("Матрицы пустые!");
            task3();
        }
        Matrix multiply = new Matrix(matrix1, matrix2);
        multiply.multiply();

        selectData();
    }

    public void insertData() {
        System.out.println("Сохраняю в таблицу...");
        String query = "INSERT INTO " + table + " (matrix1, matrix2, matrixMult) VALUES (?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setArray(1, con.createArrayOf("INTEGER", matrix1));
            pst.setArray(2, con.createArrayOf("INTEGER", matrix2));
            pst.setNull(3, Types.NULL);
            pst.executeUpdate();
            System.out.println("Все выполненные результаты добавлены в таблицу!");
        } catch (
                SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
        }
    }

    public void selectData() {
        System.out.println("Получаю данные...");
        String query = "SELECT * FROM " + table;
        try (PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int length = rsmd.getColumnCount();

            String[] columnNames = new String[length];
            int[] maxLength = new int[length];

            List<List<String>> rows = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
                maxLength[i] = rsmd.getColumnName(i + 1).length();
            }

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    String obj = rs.getString(i + 1);
                    obj = (obj != null) ? obj : "NULL";
                    row.add(obj);
                    if (obj.length() > maxLength[i]) {
                        maxLength[i] = obj.length();
                    }
                }
                rows.add(row);
            }

            StringBuilder border = new StringBuilder("+");
            StringBuilder header = new StringBuilder("|");

            for (int width : maxLength) {
                border.append("-".repeat(width + 2)).append("+");
            }
            System.out.println("Полученные данные из таблицы: ");
            System.out.println(border);


            for (int i = 0; i < length; i++) {
                header.append(" ").append(String.format("%-" + maxLength[i] + "s", columnNames[i])).append(" |");
            }
            System.out.println(header);
            System.out.println(border);

            for (List<String> row : rows) {
                StringBuilder rowStr = new StringBuilder("|");
                for (int i = 0; i < length; i++) {
                    String val = (i < row.size()) ? row.get(i) : "";
                    rowStr.append(" ").append(String.format("%-" + maxLength[i] + "s", val)).append(" |");
                }
                System.out.println(rowStr);
                System.out.println(border);
            }

        } catch (SQLException e) {
            System.out.println("Не удалось получить данные из таблицы, " + e.getMessage());
        }
    }

    public int[][] createMatrix() {
        int[][] matrix = new int[7][7];
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("Введите строку чисел ");
            System.out.print(i + 1);
            System.out.print(": ");
            for (int j = 0; j < matrix[i].length; j++) {
                try {
                    matrix[i][j] = Integer.parseInt(sc.next());
                } catch (NumberFormatException e) {
                    System.out.print("Неправильный тип данных в строке ");
                    System.out.println(i-- + 1);
                    break;
                }
            }
        }
        sc.nextLine();
        return matrix;
    }
}
