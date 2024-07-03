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
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startView.getCreateGameButton()) {
            this.startView.setVisible(false);
            this.gridView.setVisible(true);
            //this.gridView.setGridValues(model.getGrid().getValues() ;
        } else if (e.getSource() == startView.getJoinGameButton()) {

        } else if (e.getSource() == gridView.getBackButton()) {
            this.gridView.setVisible(false);
            this.startView.setVisible(true);
        }
        // TODO: gestione click su singola casella della griglia
    }
}
