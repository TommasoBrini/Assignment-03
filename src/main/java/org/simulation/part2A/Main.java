package org.simulation.part2A;

import org.simulation.part2A.controller.Controller;
import org.simulation.part2A.model.Player;
import org.simulation.part2A.view.StartView;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
//        StartView startView = new StartView();
//        Player player = new Player("1", "localhost");
        StartView startView1 = new StartView();
        Player player1 = new Player("2", "localhost:8080");
//        StartView startView2 = new StartView();
//        Player player2 = new Player("3", "localhost:8888");
//        Controller controller = new Controller(startView, player);
        Controller controller1 = new Controller(startView1, player1);
//        Controller controller2 = new Controller(startView2, player2);
    }
}
