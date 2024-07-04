package org.simulation.part2A.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.simulation.part2A.utils.Utils;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class User {
    private final String id;
    private List<Grid> allGrids;
    private final Channel channel;
    private static final String EXCHANGE_NAME = "logs";

    public User(String id) throws IOException, TimeoutException {
        this.id = id;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();

        this.allGrids = new ArrayList<>();

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        channel.basicConsume(queueName, true, updateGridCallBack(), t -> {});
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

        publishGridUpdate(grid);

        /*
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueBind(gridId + ":" + id, EXCHANGE_NAME, "");

        channel.basicConsume(gridId + ":" + id, updateGridCallBack(), t -> {});*/
    }

    public void publishGridUpdate(Grid grid) throws IOException {
        String message = grid.getId() + " " + Utils.toString(grid.getGrid());
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
    }

    private DeliverCallback updateGridCallBack(){
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
