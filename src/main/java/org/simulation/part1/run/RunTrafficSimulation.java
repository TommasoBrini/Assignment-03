package org.simulation.part1.run;

import org.simulation.part1.concreteSimulation.*;
import org.simulation.part1.controller.Controller;

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
		Controller controller = new Controller(simulation, new RoadSimView());
	}
}
