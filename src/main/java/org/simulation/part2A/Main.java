package org.simulation.part2A;


import org.simulation.part2A.controller.Controller;
import org.simulation.part2A.model.User;
import org.simulation.part2A.utils.Utils;
import org.simulation.part2A.view.GridView;
import org.simulation.part2A.view.StartView;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        User user = new User("1");
        for(int i = 0; i < 2; i++){
            user.createGrid();
        }
        GridView view = new GridView();
        Controller controller = new Controller(user, view);
        view.setVisible(true);
    }

}
