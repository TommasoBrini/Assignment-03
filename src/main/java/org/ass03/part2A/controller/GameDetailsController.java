package org.ass03.part2A.controller;

import org.ass03.part2A.model.User;
import org.ass03.part2A.utils.Utils;
import org.ass03.part2A.view.GameDetailsView;
import org.ass03.part2A.view.StartView;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameDetailsController implements GridUpdateListener{
    private final GameDetailsView gameDetailsView;
    private final StartView startView;
    private final User user;
    private final int selectedGrid;

    public GameDetailsController(User user, GameDetailsView gameDetailsView, StartView startView, int selectedGrid) {
        this.user = user;
        this.gameDetailsView = gameDetailsView;
        this.startView = startView;
        this.selectedGrid = selectedGrid;
        user.addGridUpdateListener(this);
        gameDetailsView.displayGrid(user.getGrid(selectedGrid), user);
        gameDetailsView.addBackButtonListener(new BackButtonListener());
        gameDetailsView.addSubmitButtonListener(new SubmitButtonListener());
    }

    @Override
    public void onGridCreated() {

    }

    @Override
    public void onGridUpdated(int gridIndex) {
        gameDetailsView.updateGrid(user.getGrid(gridIndex - 1));
    }

    @Override
    public void onCellSelected(int gridId, int row, int col, Color color, String userId) {
        gameDetailsView.colorCell(gridId, row, col, color);
    }

    @Override
    public void onCellUnselected(int gridId, int row, int col) {
        gameDetailsView.uncolorCell(row, col);
    }

    @Override
    public void onGridCompleted(int gridId, String userId) {
        if (gameDetailsView.isVisible() && this.selectedGrid == gridId) {
            if (userId.equals(user.getId())) {
                return;
            }
            gameDetailsView.displayMessage("Congratulations! You have successfully completed the game.");
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            gameDetailsView.setVisible(false);
            startView.setVisible(true);
        }
    }

    class SubmitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (Utils.submit(user.getGrid(selectedGrid).getGrid())) {
                try {
                    user.submitGrid(selectedGrid);
                    gameDetailsView.updateGrid(user.getGrid(selectedGrid));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                gameDetailsView.displayMessage("Congratulations! You have successfully completed the game.");
            } else {
                gameDetailsView.displayMessage("Sorry, the solution is not correct. Please try again.");
            }
        }
    }

}
