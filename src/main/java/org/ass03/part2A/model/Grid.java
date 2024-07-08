package org.ass03.part2A.model;

import org.ass03.part2A.utils.Utils;

import java.util.Arrays;

public class Grid {

    private final int id;
    private final Cell[][] grid;
    private boolean completed = false;

    public Grid(int id) {
        this.id = id;
        this.grid = this.generateGrid(Utils.generateInitialGrid());
    }

    public Grid(int id, int[][] initialGrid) {
        this.id = id;
        this.grid = this.generateGrid(initialGrid);
    }

    private Cell[][] generateGrid(int[][] initialGrid){
        Cell[][] grid = new Cell[9][9];
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                grid[row][col] = new Cell(initialGrid[row][col]);
            }
        }
        return grid;
    }

    public void setCellValue(int row, int col, int value) {
        if(!isValidValue(value)){
            return;
        }
        grid[row][col].setValue(value);
    }

    public void printGrid() {
        for (Cell[] row : grid) {
            System.out.println(Arrays.toString(Arrays.stream(row).mapToInt(Cell::getValue).toArray()));
        }
    }

    private boolean isValidValue(int value){
        return value >= 0 && value <= 9;
    }

    public int getId() {
        return id;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
    }
}
