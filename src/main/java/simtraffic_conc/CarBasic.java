package simtraffic_conc;

import java.util.Optional;

/**
 * 
 * Basic Car behaviour, considering only the presence
 * of a car in front.
 * 
 * If there is a car and it is near, slow down.
 * If there are no cars or the car is far, accelerate up to a constant speed
 * 
 */
public class CarBasic extends AbstractCar {

	private static final int CAR_NEAR_DIST = 15;
	private static final int CAR_FAR_ENOUGH_DIST = 20;
	private static final int MAX_WAITING_TIME = 2;

	private enum CarAgentState { 
				STOPPED, ACCELERATING, 
				DECELERATING_BECAUSE_OF_A_CAR, 
				WAIT_A_BIT, MOVING_CONSTANT_SPEED}	
	
	private CarAgentState state;

	private int waitingTime;
	
	public CarBasic(String id, RoadsEnv env, Road road, 
					double initialPos, 
					double acc, 
					double dec,
					double vmax) {
		super(id, road, initialPos, acc, dec, vmax);
		state = CarAgentState.STOPPED;
	}
	

	/**
	 * 
	 * Behaviour defined by a simple finite state machine 
	 *
	 */
	protected void decide(int dt) {
		switch (state) {
		case CarAgentState.STOPPED:
			if (!detectedNearCar()) {
				state = CarAgentState.ACCELERATING;
			}
			break;
		case CarAgentState.ACCELERATING:
			if (detectedNearCar()) {
				state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
			} else {
				this.currentSpeed += acceleration * dt;
				if (currentSpeed >= maxSpeed) {
					state = CarAgentState.MOVING_CONSTANT_SPEED;
				}			
			}
			break;
		case CarAgentState.MOVING_CONSTANT_SPEED:
			if (detectedNearCar()) {
				state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
			} 
			break;
		case CarAgentState.DECELERATING_BECAUSE_OF_A_CAR:
			this.currentSpeed -= deceleration * dt;
			if (this.currentSpeed <= 0) {
				state =  CarAgentState.STOPPED;
			} else if (this.carFarEnough()) {
				state = CarAgentState.WAIT_A_BIT;
				waitingTime = 0;
			}
			break;
		case CarAgentState.WAIT_A_BIT:
			waitingTime += dt;
			if (waitingTime > MAX_WAITING_TIME) {
				state = CarAgentState.ACCELERATING;
			}
			break;
		}
		
		if (currentSpeed > 0) {
			selectedAction = Optional.of(new MoveForward(getId(), currentSpeed * dt));
		}

	}
	
	/* aux methods */
		
	private boolean detectedNearCar() {
		Optional<AbstractCar> car = currentPercept.nearestCarInFront();
		if (car.isEmpty()) {
			return false;
		} else {
			double dist = car.get().getPos() - currentPercept.roadPos();
			return dist < CAR_NEAR_DIST;
		}
	}
	

	private boolean carFarEnough() {
		Optional<AbstractCar> car = currentPercept.nearestCarInFront();
		if (car.isEmpty()) {
			return true;
		} else {
			double dist = car.get().getPos() - currentPercept.roadPos();
			return dist > CAR_FAR_ENOUGH_DIST;
		}
	}

}
