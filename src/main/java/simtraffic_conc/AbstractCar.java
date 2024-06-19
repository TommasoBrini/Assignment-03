package simtraffic_conc;

import java.util.Optional;

import simengine_conc.*;
import simengine_conc.AbstractAgent;
import simengine_conc.AbstractEnvironment;
import simengine_conc.Action;

/**
 * 
 * Base class for modeling a car in the traffic environment
 * 
 */
public abstract class AbstractCar extends AbstractAgent {
	
	/* car model */
	protected double maxSpeed;		
	protected double currentSpeed;  
	protected double acceleration;
	protected double deceleration;

	/* percept and action retrieved and submitted at each step */
	protected CarPercept currentPercept;
	protected Optional<Action> selectedAction;
	
	protected Road road;
	protected double currentPos;
	
	public AbstractCar(String id, Road road, 
			double initialPos, 
			double acc, 
			double dec,
			double vmax) {
		super(id);
		this.acceleration = acc;
		this.deceleration = dec;
		this.maxSpeed = vmax;
		this.road = road;
		this.currentPos = initialPos;
	}

	/**
	 * 
	 * Basic behaviour of a car agent structured into a sense/decide/act structure 
	 * 
	 */
	public void step(int dt) {

		/* sense */

		AbstractEnvironment env = this.getEnv();
		currentPercept = (CarPercept) env.getCurrentPercepts(getId());			

		/* decide */
		
		selectedAction = Optional.empty();
		
		decide(dt);
		
		/* act */
		
		if (selectedAction.isPresent()) {
			env.submitAction(getId(),selectedAction.get());
		}
	}
	
	/**
	 * 
	 * Base method to define the behaviour strategy of the car
	 * 
	 * @param dt
	 */
	protected abstract void decide(int dt);
	
	public double getCurrentSpeed() {
		return currentSpeed;
	}
	
	protected void log(String msg) {
		System.out.println("[CAR " + this.getId() + "] " + msg);
	}

	public double getPos() {
		return currentPos;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public void updatePos(double newPos) {
		this.currentPos = newPos;
	}
	
}
