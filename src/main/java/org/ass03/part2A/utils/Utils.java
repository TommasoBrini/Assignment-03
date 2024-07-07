package org.ass03.part2A.utils;

import org.ass03.part2A.model.Cell;
import org.ass03.part2A.model.Grid;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utils {

    private static final Map<String, Color> colorMap = new HashMap<>();

    static {
        colorMap.put("red", Color.RED);
        colorMap.put("green", Color.GREEN);
        colorMap.put("yellow", Color.YELLOW);
    }

    public static Color getColorByName(String colorName) {
        return colorMap.getOrDefault(colorName.toLowerCase(), Color.BLACK); // Default to black if not found
    }

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
        int cellsToRemove = (int) (81 * 0.05);
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

    public static String toString(Cell[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                sb.append(cell.getValue()).append(" ");
            }
        }
        return sb.toString();
    }

    public static Grid fromString(String message) {
        String[] parts = message.split(" ");
        int[][] grid = new int[9][9];
        int index = 1;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col] = Integer.parseInt(parts[index]);
                index++;
            }
        }

        return new Grid(Integer.parseInt(parts[0]), grid);
    }

    public static boolean submit(Cell[][] grid) {
        // Check if each row contains all numbers from 1 to 9
        for (int row = 0; row < 9; row++) {
            boolean[] rowCheck = new boolean[9];
            for (int col = 0; col < 9; col++) {
                int value = grid[row][col].getValue();
                if (value < 1 || value > 9 || rowCheck[value - 1]) {
                    return false; // Invalid number or duplicate in the row
                }
                rowCheck[value - 1] = true;
            }
        }

        // Check if each column contains all numbers from 1 to 9
        for (int col = 0; col < 9; col++) {
            boolean[] colCheck = new boolean[9];
            for (int row = 0; row < 9; row++) {
                int value = grid[row][col].getValue();
                if (value < 1 || value > 9 || colCheck[value - 1]) {
                    return false; // Invalid number or duplicate in the column
                }
                colCheck[value - 1] = true;
            }
        }

        // Check if each 3x3 sub-grid contains all numbers from 1 to 9
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                boolean[] boxCheck = new boolean[9];
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int value = grid[boxRow * 3 + row][boxCol * 3 + col].getValue();
                        if (value < 1 || value > 9 || boxCheck[value - 1]) {
                            return false; // Invalid number or duplicate in the 3x3 sub-grid
                        }
                        boxCheck[value - 1] = true;
                    }
                }
            }
        }

        return true;

    }

}
