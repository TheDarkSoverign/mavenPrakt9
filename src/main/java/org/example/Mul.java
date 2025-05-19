package org.example;

public final class Mul extends ArrayPI {
    public int[][] calculate() {
        int[][] matrix = new int[7][7];
        int[][] matrix1 = Main.arrayPI.getMatrix1();
        int[][] matrix2 = Main.arrayPI.getMatrix2();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    matrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return matrix;
    }
}
