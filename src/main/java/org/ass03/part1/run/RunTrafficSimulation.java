package org.ass03.part1.run;

import org.ass03.part1.concreteSimulation.*;
import org.ass03.part1.controller.SimViewController;

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
		SimViewController controller = new SimViewController(simulation, new RoadSimView());
	}
}
