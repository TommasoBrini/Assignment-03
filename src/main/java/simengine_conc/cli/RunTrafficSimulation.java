package simengine_conc.cli;

import simengine_conc.*;
import simtraffic_conc_examples.*;
import simengine_conc.Flag;
import simtraffic_conc_examples.TrafficSimulationWithCrossRoads;

/**
 * 
 * Main class to create and run a simulation - CLI
 * 
 */
public class RunTrafficSimulation {

	private static final int DEFAULT_STEPS = 10000;

	public static void main(String[] args) {		

		int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
		
		// var simulation = new TrafficSimulationSingleRoadTwoCars();
		// var simulation = new TrafficSimulationSingleRoadSeveralCars();
		// var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();

		var simulation = new TrafficSimulationWithCrossRoads();
		simulation.configureNumWorkers(nWorkers);
		simulation.setup();
		
		RoadSimStatistics stat = new RoadSimStatistics();
		RoadSimView view = new RoadSimView();
		view.display();
		
		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);		
		
		Flag stopFlag = new Flag();
		simulation.run(DEFAULT_STEPS, stopFlag, true);
	}
}
