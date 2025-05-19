package org.example;

import java.util.Arrays;
import java.util.Scanner;

public final class Pow extends ArrayPI {
    public int[][] calculate(int choose, Scanner sc) {
        int[][] baseMatrix;

        if (choose == 1) {
            baseMatrix = Main.arrayPI.getMatrix1();
        } else {
            baseMatrix = Main.arrayPI.getMatrix2();
        }
        int[][] matrix = new int[7][7];

        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Arrays.copyOf(matrix[i], baseMatrix[i].length);
        }
        System.out.println("Введите степень, в которую будет возводиться выбранная матрица: ");
        int power = -1;
        while (power < 0) {
            String s = sc.nextLine();
            try {
                power = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода");
            }
            if (power < 0) {
                System.out.println("Введите целое положительное число(>= 0) ");
            }
        }

        if (power == 0) {
            matrix = new int[][]{
                    {1, 0, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 0, 0, 1}
            };
            return matrix;
        } else if (power == 1) {
            return matrix;
        } else {
            for (int p = 1; p < power; p++) {
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix.length; j++) {
                        for (int k = 0; k < matrix.length; k++) {
                            result[i][j] += matrix[i][k] * baseMatrix[k][j];
                        }
                    }
                }
                matrix = result;
            }
            return result;
        }
    }
}
