package org.ass03.part2A.model;

import org.ass03.part2A.utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class Grid {

    private final int id;
    private final Cell[][] grid;
    private boolean completed = false;

    public Grid(int id) throws IOException, TimeoutException {
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

    public int getCellValue(int row, int col){
        return grid[row][col].getValue();
    }

    public void setCellValue(int row, int col, int value) throws IOException {
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
