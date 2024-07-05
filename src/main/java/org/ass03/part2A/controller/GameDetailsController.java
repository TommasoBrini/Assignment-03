package org.ass03.part2A.controller;

import org.ass03.part2A.model.User;
import org.ass03.part2A.view.GameDetailsView;
import org.ass03.part2A.view.StartView;

import java.awt.event.ActionListener;

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
