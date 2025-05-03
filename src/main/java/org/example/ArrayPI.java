package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ArrayPI extends Main {

    public void task1() {
        String query = "SELECT table_name AS Названия_таблиц FROM postgres.information_schema.tables WHERE table_schema = 'public'";
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
        String query = "CREATE TABLE IF NOT EXISTS " + table + " (id SERIAL)";
        try {
            Statement st = con.createStatement();
            st.executeUpdate(query);
            System.out.println("Таблица " + table + " успешно создана/выбрана!");
        } catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
            task2();
        }
    }

    public void task3() {
        //Ввод матриц
    }


    public void insertData() {
        String query = "INSERT INTO " + table + " (ID) VALUES (?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setObject(1, 1);
            pst.executeUpdate();
            System.out.println("Все выполненные результаты добавлены в таблицу!");
        } catch (
                SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
        }
    }
}
