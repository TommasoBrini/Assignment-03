package org.simulation.actors.environment;

import org.simulation.seq.car.AbstractAgent;
import org.simulation.seq.environment.AbstractEnvironment;

import java.util.List;

public interface SimulationListener {

	/**
	 * Called at the beginning of the simulation
	 * 
	 * @param t
	 */
	void notifyInit(int t);
	
	/**
	 * Called at each step, updater all updates
	 * @param t
	 */
	void notifyStepDone(int t);
}
