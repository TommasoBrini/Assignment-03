package org.ass03.part2B.run;


import org.ass03.part2B.controller.StartController;
import org.ass03.part2B.model.User;
import org.ass03.part2B.view.StartView;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Client {
    public static void main(String[] args) throws IOException, NotBoundException {

        User user1 = new User("1", "yellow");
        StartView view = new StartView(user1.getId());
        new StartController(view, user1);
        view.setVisible(true);

        User user2 = new User("2", "green");
        StartView view2 = new StartView(user2.getId());
        new StartController(view2, user2);
        view2.setVisible(true);

        User user3 = new User("3", "red");
        StartView view3 = new StartView(user3.getId());
        new StartController(view3, user3);
        view3.setVisible(true);
    }
}
