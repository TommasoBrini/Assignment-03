package org.simulation.part2A.controller;

import org.simulation.part2A.model.Player;
import org.simulation.part2A.view.GridView;
import org.simulation.part2A.view.StartView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    private final StartView startView;
    private final GridView gridView;
    private final Player model;

    public Controller(StartView startView, Player model) {
        this.startView = startView;
        this.model = model;
        this.gridView = new GridView();

        this.startView.getCreateGameButton().addActionListener(this);
        this.startView.getJoinGameButton().addActionListener(this);

        this.gridView.getBackButton().addActionListener(this);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.gridView.getGrid()[i][j].addActionListener(this);
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startView.getCreateGameButton()) {
            startView.setVisible(false);
            gridView.setVisible(true);
        } else if (e.getSource() == startView.getJoinGameButton()) {

        } else if (e.getSource() == gridView.getBackButton()) {
            gridView.setVisible(false);
            startView.setVisible(true);
        } // TODO: gestione click su singola casella della griglia
    }
}
