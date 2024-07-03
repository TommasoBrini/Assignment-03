package org.simulation.part2A.utils;

import java.util.Random;

public class Utils {

    // return a grid 9x9 filled with a valid Sudoku solution
    public static int[][] generateInitialGrid() {
        int[][] grid = new int[9][9];
        fillGrid(grid);
        removeNumbers(grid);
        return grid;
    }

    private static boolean fillGrid(int[][] grid) {
        Random random = new Random();
        return fillCell(grid, 0, 0, random);
    }

    private static boolean fillCell(int[][] grid, int row, int col, Random random) {
        if (row == 9) {
            return true;
        }
        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col + 1) % 9;
        if (grid[row][col] != 0) {
            return fillCell(grid, nextRow, nextCol, random);
        }
        int[] numbers = generateRandomOrder();
        for (int num : numbers) {
            if (isValid(grid, row, col, num)) {
                grid[row][col] = num;
                if (fillCell(grid, nextRow, nextCol, random)) {
                    return true;
                }
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private static int[] generateRandomOrder() {
        int[] order = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random random = new Random();
        for (int i = 0; i < order.length; i++) {
            int swapIndex = random.nextInt(order.length);
            int temp = order[i];
            order[i] = order[swapIndex];
            order[swapIndex] = temp;
        }
        return order;
    }

    private static boolean isValid(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void removeNumbers(int[][] grid) {
        Random random = new Random();
        int cellsToRemove = (int) (81 * 0.7);
        for (int i = 0; i < cellsToRemove; i++) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (grid[row][col] == 0) {
                i--;
                continue;
            }
            grid[row][col] = 0;
        }
    }
}
