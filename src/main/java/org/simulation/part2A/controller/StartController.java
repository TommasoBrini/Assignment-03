package org.simulation.part2A.controller;

import org.simulation.part2A.model.User;
import org.simulation.part2A.view.GridView;
import org.simulation.part2A.view.StartView;

public class StartController {

    private final StartView startView;
    private final User user;

    public StartController(StartView startView, User user) {
        this.startView = startView;
        this.user = user;
        this.startView.addJoinGameListener(new OpenGridViewListener());
    }

    class OpenGridViewListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            startView.setVisible(false);
            GridView gridView = new GridView();
            new GridController(user, startView, gridView);
            gridView.setVisible(true);
        }
    }

}
