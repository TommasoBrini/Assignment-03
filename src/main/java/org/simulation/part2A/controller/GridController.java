package org.simulation.part2A.controller;

import org.simulation.part2A.model.Grid;
import org.simulation.part2A.model.User;
import org.simulation.part2A.view.GameDetailsView;
import org.simulation.part2A.view.GridView;
import org.simulation.part2A.view.StartView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridController {
    private final User user;
    private final GridView gridView;
    private final StartView startView;
    private GameDetailsView detailsView;


    public GridController(User user, StartView startView, GridView gridView) {
        this.user = user;
        this.gridView = gridView;
        this.startView = startView;
        this.gridView.addBackButtonListener(new BackButtonListener());
        initView();
    }

    private void initView() {
        gridView.displayGrids(user.getAllGrids(), new GridButtonListener());
    }

    class GridButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int gridIndex = Integer.parseInt(e.getActionCommand().split(" ")[1]) - 1;
            Grid selectedGrid = user.getGrid(gridIndex);
            detailsView = new GameDetailsView();
            detailsView.displayGrid(selectedGrid);
            detailsView.addBackButtonListener(new BackButtonListener());
            detailsView.setVisible(true);
            startView.setVisible(false);
            gridView.setVisible(false);
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gridView.setVisible(false);

            if (detailsView != null) {
                    detailsView.setVisible(false);
            }

            startView.setVisible(true);
        }
    }
}
