package org.simulation.part2A.controller;

import org.simulation.part2A.model.Grid;
import org.simulation.part2A.model.User;
import org.simulation.part2A.view.GameDetailsView;
import org.simulation.part2A.view.StartView;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class GameDetailsController {
    private final GameDetailsView gameDetailsView;
    private final StartView startView;

    public GameDetailsController(User user, GameDetailsView gameDetailsView, StartView startView, int selectedGrid) {
        this.gameDetailsView = gameDetailsView;
        this.startView = startView;
        gameDetailsView.displayGrid(user.getGrid(selectedGrid), user);
        gameDetailsView.addBackButtonListener(new BackButtonListener());
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            gameDetailsView.setVisible(false);
            startView.setVisible(true);
        }
    }
}
