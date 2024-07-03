package org.simulation.part2A;


import org.simulation.part2A.controller.GridController;
import org.simulation.part2A.controller.StartController;
import org.simulation.part2A.model.User;
import org.simulation.part2A.view.StartView;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        User user = new User("1");
        for(int i = 0; i < 10; i++){
            user.createGrid();
        }
        user.getGrid(0).printGrid();
        user.getGrid(4).printGrid();
        StartView view = new StartView();
        new StartController(view, user);
        view.setVisible(true);
    }

}
