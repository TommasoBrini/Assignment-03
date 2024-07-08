package org.ass03.part1.controller;


import org.ass03.part1.concreteSimulation.RoadSimStatistics;
import org.ass03.part1.concreteSimulation.RoadSimView;
import org.ass03.part1.environment.AbstractSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimViewController {

    private final AbstractSimulation simulation;
    private final RoadSimView view;

    public SimViewController(AbstractSimulation simulation, RoadSimView view) {
        this.simulation = simulation;
        this.view = view;

        RoadSimStatistics stat = new RoadSimStatistics(simulation.getNumCars());
        view.display();

        simulation.addSimulationListener(stat);
        simulation.addSimulationListener(view);

        view.addStartListener(new StartButtonListener());
        view.addStopListener(new StopButtonListener());
        view.addPauseListener(new PauseButtonListener());

    }

    public void runSimulation(int numSteps){
        simulation.run(numSteps);
    }

    public class StartButtonListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            runSimulation(view.getSlider().getValue());
            view.getStartButton().setBackground(Color.white);
            view.getStopButton().setBackground(Color.red);
            view.getStartButton().setEnabled(false);
            view.getStopButton().setEnabled(true);
            view.getPauseButton().setEnabled(true);
        }
    }

    public class StopButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            view.getStopButton().setEnabled(false);
			view.getPauseButton().setEnabled(false);
			JOptionPane.showMessageDialog(view, "Simulation closed");
            simulation.shutdown();
			System.exit(0);
        }
    }

    public class PauseButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            view.getStartButton().setBackground(Color.white);
            if(view.getPauseButton().getText().equals("Resume")){
                simulation.resume();
                view.getPauseButton().setText("Pause");
                view.getStopButton().setBackground(Color.red);
                view.getStopButton().setEnabled(true);
            } else {
                simulation.pause();
                view.getPauseButton().setText("Resume");
                view.getStopButton().setBackground(Color.white);
                view.getStopButton().setEnabled(false);
            }
        }
    }

}
