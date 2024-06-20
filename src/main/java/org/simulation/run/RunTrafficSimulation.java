package org.simulation.run;

import org.simulation.concreteSimulation.TrafficSimulationSingleRoadWithTrafficLightTwoCars;
import org.simulation.concreteSimulation.RoadSimStatistics;
import org.simulation.concreteSimulation.RoadSimView;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {		

		// var simulation = new TrafficSimulationSingleRoadTwoCars();
		// var simulation = new TrafficSimulationSingleRoadSeveralCars();
		 var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
		//var simulation = new TrafficSimulationWithCrossRoads();
		simulation.setup();
		
		RoadSimStatistics stat = new RoadSimStatistics();
		RoadSimView view = new RoadSimView();
		view.display();
		
		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);		
		simulation.run(10000);
	}
}
