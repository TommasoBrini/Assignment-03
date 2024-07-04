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
        /*for(int i = 0; i < 10; i++){
            user.createGrid();
        }*/
        StartView view = new StartView();
        new StartController(view, user);
        view.setVisible(true);

        /*
        User user2 = new User("2");
        StartView view2 = new StartView();
        new StartController(view2, user2);
        view2.setVisible(true);

        User user3 = new User("3");
        StartView view3 = new StartView();
        new StartController(view3, user3);
        view3.setVisible(true);*/
    }

}
