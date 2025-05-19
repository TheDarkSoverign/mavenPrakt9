package org.example;

class Sum extends ArrayPI {
    public int[][] calculate() {
        int[][] matrix = new int[7][7];
        int[][] matrix1 = Main.arrayPI.getMatrix1();
        int[][] matrix2 = Main.arrayPI.getMatrix2();

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[i].length; j++) {
                matrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return matrix;
    }
}
