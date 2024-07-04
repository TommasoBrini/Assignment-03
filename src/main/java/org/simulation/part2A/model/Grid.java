package org.simulation.part2A.model;

import org.simulation.part2A.utils.Utils;

import java.util.Arrays;

public class Grid {

    private final int id;
    private final Cell[][] grid;

    public Grid(int id) {
        this.id = id;
        this.grid = this.generateGrid(Utils.generateInitialGrid());
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

    public void setCellValue(int row, int col, int value){
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
}
