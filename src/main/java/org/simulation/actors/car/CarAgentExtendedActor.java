package org.simulation.actors.car;

import org.simulation.seq.car.CarAgentExtended;
import org.simulation.seq.car.CarAgentInfo;
import org.simulation.seq.environment.Road;
import org.simulation.seq.environment.RoadsEnv;
import org.simulation.seq.util.MoveForward;
import org.simulation.seq.util.TrafficLightInfo;

import java.util.Optional;

public class CarAgentExtendedActor extends CarAgentActor{
    private static final int CAR_NEAR_DIST = 15;
    private static final int CAR_FAR_ENOUGH_DIST = 20;
    private static final int MAX_WAITING_TIME = 2;
    private static final int SEM_NEAR_DIST = 100;

    private enum CarAgentState { STOPPED, ACCELERATING,
        DECELERATING_BECAUSE_OF_A_CAR,
        DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM,
        WAITING_FOR_GREEN_SEM,
        WAIT_A_BIT, MOVING_CONSTANT_SPEED}

    private CarAgentState state;

    private int waitingTime;

    public CarAgentExtendedActor(String id, double acc, double dec, double vmax) {
        super(id, acc, dec, vmax);
        state = CarAgentState.STOPPED;
    }

    @Override
    protected void decide(int dt) {
        switch (state) {
            case STOPPED:
                if (!detectedNearCar()) {
                    state = CarAgentState.ACCELERATING;
                }
                break;
            case ACCELERATING:
                if (detectedNearCar()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
                } else if (detectedRedOrOrgangeSemNear()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM;
                } else {
                    this.currentSpeed += acceleration * dt;
                    if (currentSpeed >= maxSpeed) {
                        state = CarAgentState.MOVING_CONSTANT_SPEED;
                    }
                }
                break;
            case MOVING_CONSTANT_SPEED:
                if (detectedNearCar()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
                } else if (detectedRedOrOrgangeSemNear()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM;
                }
                break;
            case DECELERATING_BECAUSE_OF_A_CAR:
                this.currentSpeed -= deceleration * dt;
                if (this.currentSpeed <= 0) {
                    state =  CarAgentState.STOPPED;
                } else if (this.carFarEnough()) {
                    state = CarAgentState.WAIT_A_BIT;
                    waitingTime = 0;
                }
                break;
            case DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM:
                this.currentSpeed -= deceleration * dt;
                if (this.currentSpeed <= 0) {
                    state =  CarAgentState.WAITING_FOR_GREEN_SEM;
                } else if (!detectedRedOrOrgangeSemNear()) {
                    state = CarAgentState.ACCELERATING;
                }
                break;
            case WAIT_A_BIT:
                waitingTime += dt;
                if (waitingTime > MAX_WAITING_TIME) {
                    state = CarAgentState.ACCELERATING;
                }
                break;
            case WAITING_FOR_GREEN_SEM:
                if (detectedGreenSem()) {
                    state = CarAgentState.ACCELERATING;
                }
                break;
        }

        if (currentSpeed > 0) {
            selectedAction = Optional.of(new MoveForward(getId(), currentSpeed * dt));
        }

    }

    private boolean detectedNearCar() {
        Optional<CarAgentInfo> car = currentPercept.nearestCarInFront();
        if (car.isEmpty()) {
            return false;
        } else {
            double dist = car.get().getPos() - currentPercept.roadPos();
            return dist < CAR_NEAR_DIST;
        }
    }

    private boolean detectedRedOrOrgangeSemNear() {
        Optional<TrafficLightInfo> sem = currentPercept.nearestSem();
        if (sem.isEmpty() || sem.get().sem().isGreen()) {
            return false;
        } else {
            double dist = sem.get().roadPos() - currentPercept.roadPos();
            return dist > 0 && dist < SEM_NEAR_DIST;
        }
    }

    private boolean detectedGreenSem() {
        Optional<TrafficLightInfo> sem = currentPercept.nearestSem();
        return (!sem.isEmpty() && sem.get().sem().isGreen());
    }

    private boolean carFarEnough() {
        Optional<CarAgentInfo> car = currentPercept.nearestCarInFront();
        if (car.isEmpty()) {
            return true;
        } else {
            double dist = car.get().getPos() - currentPercept.roadPos();
            return dist > CAR_FAR_ENOUGH_DIST;
        }
    }
}
