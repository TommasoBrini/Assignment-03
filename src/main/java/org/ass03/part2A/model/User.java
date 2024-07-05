package org.ass03.part2A.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.ass03.part2A.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class User {
    private final String id;
    private List<Grid> allGrids;
    private final Channel channel;
    private static final String EXCHANGE_CREATE = "create";
    private static final String EXCHANGE_UPDATE = "update";

    public User(String id) throws IOException, TimeoutException {
        this.id = id;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();

        this.allGrids = new ArrayList<>();

        channel.exchangeDeclare(EXCHANGE_CREATE, "fanout");
        channel.exchangeDeclare(EXCHANGE_UPDATE, "fanout");

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_CREATE, "");
        channel.basicConsume(queueName, true, addGridCallBack(), t -> {});

        String updateQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(updateQueueName, EXCHANGE_UPDATE, "");
        channel.basicConsume(updateQueueName, true, updateGridCallBack(), t -> {});
    }

    public String getId() {
        return id;
    }

    public List<Grid> getAllGrids() {
        return allGrids;
    }

    public void createGrid() throws IOException, TimeoutException {
        int gridId = allGrids.size() + 1;
        Grid grid = new Grid(gridId, id);
        allGrids.add(grid);

        publishGrid(grid);

        /*
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueBind(gridId + ":" + id, EXCHANGE_NAME, "");

        channel.basicConsume(gridId + ":" + id, updateGridCallBack(), t -> {});*/
    }

    public void publishGrid(Grid grid) throws IOException {
        String message = grid.getId() + " " + Utils.toString(grid.getGrid());
        channel.basicPublish(EXCHANGE_CREATE, "", null, message.getBytes("UTF-8"));
    }

    public void updateGrid(int gridId, int row, int col, int value) throws IOException {
        String message = gridId + " " + row + " " + col + " " + value;
        channel.basicPublish(EXCHANGE_UPDATE, "", null, message.getBytes("UTF-8"));
    }

    private DeliverCallback updateGridCallBack(){
        return (consumerTag, delivery) -> { //TODO: non deve aggiornare anche se stesso
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            String[] parts = message.split(" ");
            int gridId = Integer.parseInt(parts[0]);
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            int value = Integer.parseInt(parts[3]);
            try {
                allGrids.get(gridId - 1).setCellValue(row, col, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private DeliverCallback addGridCallBack(){
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            Grid receivedGrid = Utils.fromString(message);

            if (allGrids.stream().noneMatch(grid -> grid.getId() == (receivedGrid.getId()))) {
                allGrids.add(receivedGrid);
            }

//            String[] parts = message.split(" ");
//            int row = Integer.parseInt(parts[1]);
//            int col = Integer.parseInt(parts[2]);
//            int value = Integer.parseInt(parts[4]);
//            try {
//                grid.setCellValue(row, col, value);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        };
    }

    public Grid getGrid(int index){
        return allGrids.get(index);
    }

}
