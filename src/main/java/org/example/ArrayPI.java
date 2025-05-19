package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class ArrayPI {
    private int[][] matrix1;
    private int[][] matrix2;

    Scanner sc = new Scanner(System.in);

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

    public void setMatrix1() {
        matrix1 = createMatrix();
    }

    public int[][] getMatrix1() {
        return matrix1;
    }

    public void setMatrix2() {
        matrix2 = createMatrix();
    }

    public int[][] getMatrix2() {
        return matrix2;
    }

    public boolean checkMatrix() {
        if (matrix1 == null || matrix2 == null) {
            System.out.println("Матрицы пустые!");
            return false;
        } else return true;
    }

    public void printMatrix(int[][] matrix) {
        for (int[] i : matrix) {
            System.out.println(Arrays.toString(i));
        }
    }
}

