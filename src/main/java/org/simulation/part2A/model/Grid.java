package org.simulation.part2A.model;

public class Grid {
    private final int[][] grid;

    public Grid() {
        this.grid = new int[9][9];
    }

    public int getCell(int row, int col) {
        return this.grid[row][col];
    }

    public void setCell(int row, int col, int value) {
        this.grid[row][col] = value;
    }
}
