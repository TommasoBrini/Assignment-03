package simtraffic_conc_examples;

import simengine_conc.*;
import simengine_conc.AbstractSimulation;
import simtraffic_conc.*;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, with one semaphore
 * 
 */
public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadWithTrafficLightTwoCars() {
		super();
	}
	
	public void setup() {

		this.setTimings(0, 1);
		
		RoadsEnv env = new RoadsEnv();
		this.setEnvironment(env);
				
		Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));

		TrafficLight tl = env.createTrafficLight(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
		r.addTrafficLight(tl, 740);
		
		AbstractCar car1 = new CarExtended("car-1", env, r, 0, 0.1, 0.3, 6);
		this.addAgent(car1);		
		AbstractCar car2 = new CarExtended("car-2", env, r, 100, 0.1, 0.3, 5);
		this.addAgent(car2);
	}	
	
}
