package simtraffic_conc_examples;

import simengine_conc.*;
import simengine_conc.AbstractSimulation;
import simtraffic_conc.*;

public class TrafficSimulationWithCrossRoads extends AbstractSimulation {

	public TrafficSimulationWithCrossRoads() {
		super();
	}
	
	public void setup() {

		this.setTimings(0, 1);
		
		RoadsEnv env = new RoadsEnv();
		this.setEnvironment(env);
				
		TrafficLight tl1 = env.createTrafficLight(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
		
		Road r1 = env.createRoad(new P2d(0,300), new P2d(1500,300));
		r1.addTrafficLight(tl1, 740);
		
		AbstractCar car1 = new CarExtended("car-1", env, r1, 0, 0.1, 0.3, 6);
		this.addAgent(car1);		
		AbstractCar car2 = new CarExtended("car-2", env, r1, 100, 0.1, 0.3, 5);
		this.addAgent(car2);		
		
		TrafficLight tl2 = env.createTrafficLight(new P2d(750,290),  TrafficLight.TrafficLightState.RED, 75, 25, 100);

		Road r2 = env.createRoad(new P2d(750,0), new P2d(750,600));
		r2.addTrafficLight(tl2, 290);

		AbstractCar car3 = new CarExtended("car-3", env, r2, 0, 0.1, 0.2, 5);
		this.addAgent(car3);		
		AbstractCar car4 = new CarExtended("car-4", env, r2, 100, 0.1, 0.1, 4);
		this.addAgent(car4);
	}	
	
}
