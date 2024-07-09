package org.ass03.part2B.model;

import org.ass03.part2B.model.remote.Grid;
import org.ass03.part2B.utils.Utils;

import java.util.Arrays;

public class GridImpl implements Grid {

    private final int id;
    private final Cell[][] grid;
    private boolean completed = false;

    public GridImpl(int id) {
        this.id = id;
        this.grid = this.generateGrid(Utils.generateInitialGrid());
    }

    public GridImpl(int id, int[][] initialGrid) {
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

    @Override
    public void setCellValue(int row, int col, int value) {
        if(!isValidValue(value)){
            return;
        }
        grid[row][col].setValue(value);
    }

    @Override
    public void printGrid() {
        for (Cell[] row : grid) {
            System.out.println(Arrays.toString(Arrays.stream(row).mapToInt(Cell::getValue).toArray()));
        }
    }

    private boolean isValidValue(int value){
        return value >= 0 && value <= 9;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Cell[][] getGrid() {
        return grid;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setCompleted() {
        this.completed = true;
    }
}
