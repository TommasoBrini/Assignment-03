package org.simulation.part2A;

import org.simulation.part2A.controller.Controller;
import org.simulation.part2A.model.Player;
import org.simulation.part2A.view.StartView;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        StartView startView = new StartView();
        Player player = new Player("1", "localhost");
        Controller controller = new Controller(startView, player);
    }
}
