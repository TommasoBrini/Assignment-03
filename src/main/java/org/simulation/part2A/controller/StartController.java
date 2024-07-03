package org.simulation.part2A.controller;

import org.simulation.part2A.model.User;
import org.simulation.part2A.view.GameDetailsView;
import org.simulation.part2A.view.GridView;
import org.simulation.part2A.view.StartView;

import java.awt.event.ActionListener;

public class StartController {

    private final StartView startView;
    private final User user;
    private GameDetailsView gameDetailsView;

    public StartController(StartView startView, User user) {
        this.startView = startView;
        this.user = user;
        this.startView.addJoinGameListener(new OpenGridViewListener());
        this.startView.addNewGameListener(new NewGameListener());
    }

    class OpenGridViewListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            startView.setVisible(false);
            GridView gridView = new GridView();
            new GridController(user, startView, gridView);
            gridView.setVisible(true);
        }
    }

    class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            user.createGrid();
            gameDetailsView = new GameDetailsView();
            gameDetailsView.displayGrid(user.getGrid(user.getAllGrids().size() - 1));
            gameDetailsView.addBackButtonListener(new BackButtonListener());
            startView.setVisible(false);
            gameDetailsView.setVisible(true);
        }
    }

class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            gameDetailsView.setVisible(false);
            startView.setVisible(true);
        }
    }

}
