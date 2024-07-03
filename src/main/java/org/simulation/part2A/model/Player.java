package org.simulation.part2A.model;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Player {
    private static final String GRID_EXCHANGE = "SudokuGridExchange";
    private static final String SELECTION_QUEUE_PREFIX = "SelectionQueue-";

    private String playerId;
    private Connection connection;
    private Channel channel;
    private Grid grid;

    public Player(String playerId, String host) throws IOException, TimeoutException {
        this.playerId = playerId;
        this.grid = new Grid();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();

        channel.exchangeDeclare(GRID_EXCHANGE, BuiltinExchangeType.FANOUT);
    }

    public void selectCell(int row, int col) throws IOException {
        String selectionQueueName = SELECTION_QUEUE_PREFIX + row + "-" + col;
        channel.queueDeclare(selectionQueueName, false, false, false, null);

        String message = playerId;
        channel.basicPublish("", selectionQueueName, null, message.getBytes());
    }

    public void updateGrid(int row, int col, int value) throws IOException {
        String message = row + "," + col + "," + value;
        channel.basicPublish(GRID_EXCHANGE, "", null, message.getBytes());
    }

    public void joinGrid() throws IOException {
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, GRID_EXCHANGE, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            onGridMessage(message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    private void onGridMessage(String message) {
        // Handle grid update
        System.out.println("Received grid update: " + message);
    }

    public Grid getGrid() {
        return grid;
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
