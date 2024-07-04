package org.simulation.part2A.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.simulation.part2A.utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class Grid {

    private final int id;
    private final Cell[][] grid;
    private static final String EXCHANGE_NAME = "logs";
    private final Channel channel;

    public Grid(int id, String userId) throws IOException, TimeoutException {
        this.id = id;
        this.grid = this.generateGrid(Utils.generateInitialGrid());

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        channel = factory.newConnection().createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueDeclare(id + ":" + userId, false, false, false, null);

        //channel.basicPublish(EXCHANGE_NAME, "", null, (id + " " + Utils.gridToString(grid)).getBytes());
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
        String message = id + " " + row + " " + col + " " + value;
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
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
