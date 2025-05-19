package org.example;

import java.sql.*;
import java.util.*;

public class Main {
    protected static Scanner sc = new Scanner(System.in);
    protected static Connection con;
    static final String schema = "task9";
    protected static String table = "task9";

    protected static ArrayPI arrayPI = new ArrayPI();


    static String Url = "jdbc:postgresql://localhost:5432/postgres";

    static {
        try {
            con = DriverManager.getConnection(Url, "postgres", "postgres");
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных: " + e.getMessage());
        }

        try {
            con.setAutoCommit(false);

            Statement st = con.createStatement();
            st.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + schema);
            st.executeUpdate("SET search_path TO " + schema);

            con.commit();
            con.setAutoCommit(true);
            System.out.println("Используется схема - " + schema);
        } catch (SQLException e) {
            System.out.println("Не удалось создать схему для задания: " + e.getMessage());
            try {
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        String query = "CREATE TABLE IF NOT EXISTS task9 (id SERIAL, matrix1 int[], matrix2 int[], matrix_sum int[], matrix_sub int[], matrix_mul int[], matrix_pow1 int[], matrix_pow2 int[])";
        try {
            Statement st = con.createStatement();
            st.executeUpdate(query);
            table = "task9";
            System.out.println("Используется таблица по умолчанию - " + table);
        } catch (SQLException e) {
            System.out.println("Не удалось использовать таблицу по умолчанию, " + e.getMessage());
        }
    }

    protected static void menu() {
        int x = 0;
        String s = "";
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
                case 1 -> new Task1().task1();
                case 2 -> new Task2().task2();
                case 3 -> new Task3().task3();
                case 4 -> new Task4().task4();
                case 5 -> {
                    System.out.print("Введите название файла: ");
                    String filepath = sc.nextLine();

                    if (!filepath.contains(".xlsx")) {
                        filepath += ".xlsx";
                    }

                    ExportToExcel task5 = new ExportToExcel();
                    task5.exportData(filepath);
                    task5.printExcelData(filepath);
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

class Task1 extends Main {
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
}

class Task2 extends Main {
    public void task2() {
        System.out.print("Введите название таблицы: ");
        table = sc.next();
        sc.nextLine();
        String query = "CREATE TABLE IF NOT EXISTS " + table + " (id SERIAL, matrix1 int[], matrix2 int[], matrix_sum int[], matrix_sub int[], matrix_mul int[], matrix_pow1 int[], matrix_pow2 int[])";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.executeUpdate();
            System.out.println("Таблица " + table + " успешно создана/выбрана!");
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
            task2();
        }
    }
}

class Task3 extends Main {
    public void task3() {
        System.out.println("Введите матрицу 1");
        arrayPI.setMatrix1();
        System.out.println("Матрица 1: ");
        arrayPI.printMatrix(arrayPI.getMatrix1());

        System.out.println("Введите матрицу 2");
        arrayPI.setMatrix2();
        System.out.println("Матрица 2:");
        arrayPI.printMatrix(arrayPI.getMatrix2());

        insertData();
        selectData();
    }

    public void insertData() {
        System.out.println("Сохраняю в таблицу...");
        String query = "INSERT INTO " + table + " (matrix1, matrix2) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setArray(1, con.createArrayOf("INTEGER", arrayPI.getMatrix1()));
            pst.setArray(2, con.createArrayOf("INTEGER", arrayPI.getMatrix2()));
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
}

class Task4 extends Main {
    public void task4() {
        Task3 task3 = new Task3();
        if (!arrayPI.checkMatrix()) {
            System.out.println("Матрицы пустые!");
            task3.task3();
        }

        insertData();
        task3.selectData();
    }

    public void insertData() {
        int[][] matrix_sum = new Sum().calculate();
        System.out.println("Сумма матриц: ");
        arrayPI.printMatrix(matrix_sum);

        int[][] matrix_sub = new Sub().calculate();
        System.out.println("Разность матриц: ");
        arrayPI.printMatrix(matrix_sub);

        int[][] matrix_mul = new Mul().calculate();
        System.out.println("Перемноженные матрицы:");
        arrayPI.printMatrix(matrix_mul);

        int[][] matrix_pow1 = new Pow().calculate(1, sc);
        System.out.println("Матрица 1, возведенная в степень:");
        arrayPI.printMatrix(matrix_pow1);

        int[][] matrix_pow2 = new Pow().calculate(2, sc);
        System.out.println("Матрица 2, возведенная в степень:");
        arrayPI.printMatrix(matrix_pow2);

        System.out.println("Сохраняю в таблицу...");
        String query = "UPDATE " + table + " SET " +
                "matrix_sum = ?, " +
                "matrix_sub = ?, " +
                "matrix_mul = ?, " +
                "matrix_pow1 = ?, " +
                "matrix_pow2 = ? " +
                "WHERE id = (SELECT MAX(id) FROM " + table + " WHERE coalesce(matrix_sum, matrix_sub, matrix_mul, matrix_pow1, matrix_pow1) IS NULL)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setArray(1, con.createArrayOf("INTEGER", matrix_sum));
            pst.setArray(2, con.createArrayOf("INTEGER", matrix_sub));
            pst.setArray(3, con.createArrayOf("INTEGER", matrix_mul));
            pst.setArray(4, con.createArrayOf("INTEGER", matrix_pow1));
            pst.setArray(5, con.createArrayOf("INTEGER", matrix_pow2));
            pst.executeUpdate();
            System.out.println("Все выполненные результаты добавлены в таблицу!");
        } catch (
                SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
        }
    }
}