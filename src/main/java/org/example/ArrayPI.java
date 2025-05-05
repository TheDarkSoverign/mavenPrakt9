package org.example;

import java.sql.*;
import java.util.Arrays;

public class ArrayPI extends Main {
    int[][] matrix1;
    int[][] matrix2;

    public void task1() {
        String query = "SELECT table_name AS Названия_таблиц FROM information_schema.tables WHERE table_schema = '" + schema + "'";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            try {
                System.out.println("Список таблиц:");
                while (rs.next()) {
                    String tableName = rs.getString("Названия_таблиц");
                    System.out.println(tableName);
                }
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
        try {
            PreparedStatement pst = con.prepareStatement(createTable);
            pst.setString(1, table);
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
        try (PreparedStatement pst = con.prepareStatement(insertIntoTable)) {
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
        try (PreparedStatement pst = con.prepareStatement(selectFromTable)) {
            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("Полученные данные: ");
                System.out.printf("%3s | %-31s | %-31s | %-31s \n", "ID", "Матрица 1", "Матрица 2", "Перемноженная матрица");
                while (rs.next()) {
                    int ID = rs.getInt(1);

                    Array matrixArray1 = rs.getArray(2);
                    Integer[][] matrix1 = (Integer[][]) matrixArray1.getArray();

                    Array matrixArray2 = rs.getArray(3);
                    Integer[][] matrix2 = (Integer[][]) matrixArray2.getArray();

                    Array matrixArray3 = rs.getArray(4);
                    if (matrixArray3 != null) {
                        Integer[][] matrixMult = (Integer[][]) matrixArray3.getArray();
                        System.out.printf("%2d. | %-31s | %-31s | %-31s \n", ID, Arrays.toString(matrix1[0]), Arrays.toString(matrix2[0]), Arrays.toString(matrixMult[0]));
                        for (int i = 1; i < 7; i++) {
                            System.out.printf("%3s | %-31s | %-31s | %-31s \n", "", Arrays.toString(matrix1[i]), Arrays.toString(matrix2[i]), Arrays.toString(matrixMult[i]));
                        }
                    } else {
                        System.out.printf("%2d. | %-31s | %-31s | %-31s \n", ID, Arrays.toString(matrix1[0]), Arrays.toString(matrix2[0]), "NULL");
                        for (int i = 1; i < 7; i++) {
                            System.out.printf("%3s | %-31s | %-31s | %-31s \n", "", Arrays.toString(matrix1[i]), Arrays.toString(matrix2[i]), "NULL");
                        }
                    }
                }
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
