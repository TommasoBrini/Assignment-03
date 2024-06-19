package pcd.ass01.test;

import pcd.ass01.simulation.SimulationThread;
import pcd.ass01.simulation.implementation.*;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {
		//var simulation = new TrafficSimulationSingleRoadTwoCars();
		//var simulation = new TrafficSimulationSingleRoadSeveralCars();
		//var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
		var simulation = new TrafficSimulationWithCrossRoads();
		simulation.setup();

		SimulationThread simulationThread = new SimulationThread(simulation);
		String name = simulation.getClass().getName();
		String[] as = name.split("\\.");
		name = as[as.length - 1];

		RoadSimStatistics stat = new RoadSimStatistics();
		RoadSimView view = new RoadSimView(simulationThread, name);
		view.display();
		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);		
		simulation.init();
	}
}
