package org.simulation.first.actors.run;


import org.simulation.first.actors.concreteSimulation.TrafficSimulationSingleRoadMassiveNumberOfCars;

public class RunTrafficSimulationMassiveTest {

	public static void main(String[] args) {		

		int numCars = 500;
		int nSteps = 100;
		
		var simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(numCars);
		simulation.setup();
		
		log("Running the simulation: " + numCars + " cars, for " + nSteps + " steps ...");
		
		simulation.run(nSteps);

		boolean completed = false;
		while (!completed) {
			completed = simulation.isCompleted();
		}

		long d = simulation.getSimulationDuration();
		log("Completed in " + d + " ms - average time per step: " + simulation.getAverageTimePerCycle() + " ms");
		simulation.shutdown();
	}
	
	private static void log(String msg) {
		System.out.println("[ SIMULATION ] " + msg);
	}
}
