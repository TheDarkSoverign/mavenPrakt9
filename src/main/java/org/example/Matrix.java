package org.example;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public final class Matrix extends ArrayPI {
    int[][] matrix1;
    int[][] matrix2;
    int[][] matrixMult;

    Matrix(int[][] matrix1, int[][] matrix2) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    public void multiply() {
        matrixMult = new int[matrix1.length][matrix2[0].length];
        for (int i = 0; i < matrixMult.length; i++) {
            for (int j = 0; j < matrixMult.length; j++) {
                for (int k = 0; k < matrixMult.length; k++) {
                    matrixMult[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        System.out.println("Перемноженная матрица:");
        for (int[] i : matrixMult) {
            System.out.println(Arrays.toString(i));
        }

        insertData();
    }


    public void insertData() {
        System.out.println("Сохраняю в таблицу...");
        String query = "UPDATE " + table + " SET matrixMult = ? WHERE id = (SELECT MAX(id) FROM " + table + " WHERE matrixMult IS NULL)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setArray( 1, con.createArrayOf("INTEGER", matrixMult));
            pst.executeUpdate();
            System.out.println("Все выполненные результаты добавлены в таблицу!");
        } catch (
                SQLException e) {
            System.out.println("Не удалось выполнить запрос, " + e.getMessage());
        }
    }
}
