package org.ass03.part2A.controller;

import org.ass03.part2A.model.User;
import org.ass03.part2A.view.GameDetailsView;
import org.ass03.part2A.view.StartView;

import java.awt.event.ActionListener;

public class GameDetailsController implements GridUpdateListener{
    private final GameDetailsView gameDetailsView;
    private final StartView startView;
    private final User user;

    public GameDetailsController(User user, GameDetailsView gameDetailsView, StartView startView, int selectedGrid) {
        this.user = user;
        this.gameDetailsView = gameDetailsView;
        this.startView = startView;
        user.addGridUpdateListener(this);
        gameDetailsView.displayGrid(user.getGrid(selectedGrid), user);
        gameDetailsView.addBackButtonListener(new BackButtonListener());
    }

    @Override
    public void onGridCreated() {

    }

    @Override
    public void onGridUpdated(int gridIndex) {
        gameDetailsView.updateGrid(user.getGrid(gridIndex - 1));
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            gameDetailsView.setVisible(false);
            startView.setVisible(true);
        }
    }


}
