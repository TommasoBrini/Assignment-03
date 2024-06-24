package org.simulation.actors.run;

import org.simulation.actors.concreteSimulation.RoadSimStatistics;
import org.simulation.actors.concreteSimulation.RoadSimView;
import org.simulation.actors.concreteSimulation.TrafficSimulationSingleRoadTwoCars;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {		

		var simulation = new TrafficSimulationSingleRoadTwoCars();
		simulation.setup();

		RoadSimStatistics stat = new RoadSimStatistics();
		RoadSimView view = new RoadSimView();
		view.display();

		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);
		simulation.run(10);
	}
}
