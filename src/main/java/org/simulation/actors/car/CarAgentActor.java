package org.simulation.actors.car;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import org.simulation.actors.Message;
import org.simulation.seq.car.CarAgentInfo;
import org.simulation.seq.util.Action;
import org.simulation.seq.util.CarPercept;
import org.simulation.seq.util.MoveForward;
import org.simulation.seq.util.TrafficLightInfo;

import java.util.Objects;
import java.util.Optional;

public class CarAgentActor extends AbstractActor {
    private String myId;

    protected double maxSpeed;
    protected double currentSpeed;
    protected double acceleration;
    protected double deceleration;
    /* percept and action retrieved and submitted at each step */
    protected CarPercept currentPercept;
    protected Optional<Action> selectedAction;

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

    public CarAgentActor(String id, double acc, double dec, double vmax) {
        super();
        this.myId = id;
        this.acceleration = acc;
        this.deceleration = dec;
        this.maxSpeed = vmax;
        state = CarAgentState.STOPPED;
    }

    private void step(int dt) {
        System.out.println("Step for car " + getId() + "...");
        /* sense */
        /*
        TODO: ask to env the percepts
         */
        getContext().actorSelection("/user/env").tell(new Message<>("get-current-percepts", getId()), ActorRef.noSender());
        //currentPercept = (CarPercept) env.getCurrentPercepts(getId());
    }

    private void decideAndAct(int dt){
        /* decide */
        selectedAction = Optional.empty();

        decide(dt);

        /* act TODO: vero act*/
        if (selectedAction.isPresent()) {
            getContext().actorSelection("/user/env").tell(new Message<>("submit-action", selectedAction.get()), ActorRef.noSender());
        }
        //getContext().actorSelection("/user/env").tell(new Message<>("submit-action", selectedAction.get()), ActorRef.noSender());
    }

    private void decide(int dt){
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
        } else {
            selectedAction = Optional.of(new MoveForward(getId(), currentSpeed * 0));
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

    private String getId() {
        return myId;
    }

    private double getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.content()))
                .match(Message.class, message -> "percepts".equals(message.name()), message -> {
                    //TODO: update percepts
                    this.currentPercept = new CarPercept(0.1, Optional.empty(), Optional.empty());
                    this.decideAndAct((Integer) message.content());
                })
                .match(Message.class, message -> "stop".equals(message.name()), s -> getContext().stop(self()))
                .build();
    }
}
