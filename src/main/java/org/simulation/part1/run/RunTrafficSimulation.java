package org.simulation.part1.run;

import org.simulation.part1.concreteSimulation.RoadSimStatistics;
import org.simulation.part1.concreteSimulation.RoadSimView;
import org.simulation.part1.concreteSimulation.TrafficSimulationWithCrossRoads;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {		


		//var simulation = new TrafficSimulationSingleRoadTwoCars();
		var simulation = new TrafficSimulationWithCrossRoads();
		//var simulation = new TrafficSimulationSingleRoadSeveralCars();
		//var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
		simulation.setup();

		RoadSimStatistics stat = new RoadSimStatistics(simulation.getNumCars());
		RoadSimView view = new RoadSimView();
		view.display();

		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);

		simulation.run(1000);
	}
}
