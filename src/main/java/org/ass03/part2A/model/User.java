package org.ass03.part2A.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.ass03.part2A.controller.GridUpdateListener;
import org.ass03.part2A.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class User {
    private final String id;
    private List<Grid> allGrids;
    private Channel channel;
    private Connection connection;
    private static final String EXCHANGE_CREATE = "create";
    private static final String EXCHANGE_UPDATE = "update";
    private static final String EXCHANGE_SELECT = "select";
    private static final String ECXHANGE_UNSELECT = "unselect";
    private static final String EXCHANGE_SUBMIT = "submit";
    private List<GridUpdateListener> listeners;
    private final String color;

    public User(String id, String color) throws IOException, TimeoutException {
        this.id = id;
        this.color = color;
        this.setupConnection();
        this.allGrids = new ArrayList<>();
        this.listeners = new ArrayList<>();

        channel.exchangeDeclare(EXCHANGE_CREATE, "fanout");
        channel.exchangeDeclare(EXCHANGE_UPDATE, "fanout");
        channel.exchangeDeclare(EXCHANGE_SELECT, "fanout");
        channel.exchangeDeclare(ECXHANGE_UNSELECT, "fanout");
        channel.exchangeDeclare(EXCHANGE_SUBMIT, "fanout");

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_CREATE, "");
        channel.basicConsume(queueName, true, addGridCallBack(), t -> {});

        String updateQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(updateQueueName, EXCHANGE_UPDATE, "");
        channel.basicConsume(updateQueueName, true, updateGridCallBack(), t -> {});

        String selectQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(selectQueueName, EXCHANGE_SELECT, "");
        channel.basicConsume(selectQueueName, true, selectCellCallBack(), t -> {});

        String unselectQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(unselectQueueName, ECXHANGE_UNSELECT, "");
        channel.basicConsume(unselectQueueName, true, unselectCellCallBack(), t -> {});

        String submitQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(submitQueueName, EXCHANGE_SUBMIT, "");
        channel.basicConsume(submitQueueName, true, submitGridCallBack(), t -> {});
    }

    public String getColor() {
        return color;
    }

    void setupConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
    }

    public void addGridUpdateListener(GridUpdateListener listener){
        listeners.add(listener);
    }

    private void notifyGridCreated() {
        for (GridUpdateListener listener : listeners) {
            listener.onGridCreated();
        }
    }

    private void notifyGridUpdated(int gridId) {
        for (GridUpdateListener listener : listeners) {
            listener.onGridUpdated(gridId);
        }
    }

    private void notifyCellSelected(int gridId, int row, int col, Color color, String idUser) {
        for (GridUpdateListener listener : listeners) {
            listener.onCellSelected(gridId, row, col, color, idUser);
        }
    }

    private void notifyCellUnselect(int gridId, int row, int col){
        for (GridUpdateListener listener : listeners) {
            listener.onCellUnselected(gridId, row, col);
        }
    }

    private void notifyGridCompleted(int gridId, String userId) {
        for (GridUpdateListener listener : listeners) {
            listener.onGridCompleted(gridId, userId);
        }
    }

    public String getId() {
        return id;
    }

    public List<Grid> getAllGrids() {
        return allGrids;
    }

    public void createGrid() throws IOException, TimeoutException {
        int gridId = allGrids.size() + 1;
        Grid grid = new Grid(gridId);
        allGrids.add(grid);

        publishGrid(grid);
        notifyGridCreated();
    }

    public void publishGrid(Grid grid) throws IOException {
        String message = grid.getId() + " " + Utils.toString(grid.getGrid());
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_CREATE, "", null, message.getBytes("UTF-8"));
    }

    public void updateGrid(int gridId, int row, int col, int value) throws IOException {
        String message = gridId + " " + row + " " + col + " " + value;
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_UPDATE, "", null, message.getBytes("UTF-8"));
    }

    public void selectCell(int gridId, int row, int col) throws IOException {
        String message = gridId + " " + row + " " + col + " " + color + " " + id;
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_SELECT, "", null, message.getBytes("UTF-8"));
    }

    public void unselectCell(int gridId, int row, int col) throws IOException {
        String message = gridId + " " + row + " " + col;
        ensureChannelIsOpen();
        channel.basicPublish(ECXHANGE_UNSELECT, "", null, message.getBytes("UTF-8"));
    }

    public void submitGrid(int gridId) throws IOException {
        String message = gridId + " " + id;
        ensureChannelIsOpen();
        channel.basicPublish(EXCHANGE_SUBMIT, "", null, message.getBytes("UTF-8"));
    }

    private void ensureChannelIsOpen() throws IOException {
        if (channel == null || !channel.isOpen()) {
            try {
                setupConnection();
            } catch (TimeoutException e) {
                throw new IOException("Failed to reopen channel", e);
            }
        }
    }

    private DeliverCallback updateGridCallBack(){
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received update '" + message + "'");

            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            int value = Integer.parseInt(parts[3]);
            try {
                allGrids.get(gridId - 1).setCellValue(row, col, value);
                notifyGridUpdated(gridId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private DeliverCallback addGridCallBack(){
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received create '" + message + "'");

            Grid receivedGrid = Utils.fromString(message);

            if (allGrids.stream().noneMatch(grid -> grid.getId() == (receivedGrid.getId()))) {
                allGrids.add(receivedGrid);
                notifyGridCreated();
            }
        };
    }

    private DeliverCallback selectCellCallBack(){
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received select '" + message + "'");

            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            Color color = Utils.getColorByName(parts[3]);
            String idUser = parts[4];
            this.notifyCellSelected(gridId, row, col, color, idUser);
        };
    }

    private DeliverCallback unselectCellCallBack(){
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received unselect '" + message + "'");

            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            this.notifyCellUnselect(gridId, row, col);
        };
    }

    private DeliverCallback submitGridCallBack(){
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received submit '" + message + "'");

            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            String userId = parts[1];
            allGrids.get(gridId).setCompleted();
            notifyGridCompleted(gridId, userId);
        };
    }

    public Grid getGrid(int index){
        return allGrids.get(index);
    }

}
