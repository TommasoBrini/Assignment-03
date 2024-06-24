package org.simulation.actors.run;

import org.simulation.actors.concreteSimulation.*;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {		

		//var simulation = new TrafficSimulationSingleRoadTwoCars();
		//var simulation = new TrafficSimulationWithCrossRoads();
		var simulation = new TrafficSimulationSingleRoadSeveralCars();
		//var simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(5000);
		simulation.setup();

		RoadSimStatistics stat = new RoadSimStatistics();
		RoadSimView view = new RoadSimView();
		view.display();

		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);

		simulation.run(1000);
	}
}
