package org.example;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public final class Matrix extends ArrayPI {
    int[][] matrix1;
    int[][] matrix2;
    int[][] matrixMult;

    Matrix(int[][] matrix1, int[][] matrix2) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    public void multiply() {

    }

    @Override
    public void insertData() {
        System.out.println("Сохраняю в таблицу...");
        String query = "UPDATE ? SET matrixMult = ? WHERE id = (SELECT MAX(id) FROM ? WHERE matrixMult IS NULL)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, table);
            pst.setArray( 2, con.createArrayOf("INTEGER", matrixMult));
            pst.setString(3, table);
            pst.executeUpdate();
            System.out.println("Все выполненные результаты добавлены в таблицу!");
        } catch (
                SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
        }
    }
}
