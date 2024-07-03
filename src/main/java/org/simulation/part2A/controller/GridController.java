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

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startView.setVisible(true);
            gridView.dispose();
        }
    }

    class GridButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int gridIndex = Integer.parseInt(e.getActionCommand().split(" ")[1]) - 1;
            Grid selectedGrid = user.getGrid(gridIndex);
            GameDetailsView detailsView = new GameDetailsView();
            detailsView.displayGrid(selectedGrid);
            detailsView.setVisible(true);
            startView.dispose();
            gridView.dispose();
        }
    }
}
