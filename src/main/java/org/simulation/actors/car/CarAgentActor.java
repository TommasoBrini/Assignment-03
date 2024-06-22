package org.simulation.actors.car;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import org.simulation.actors.util.Message;
import org.simulation.actors.environment.Road;
import org.simulation.seq.car.CarAgentInfo;
import org.simulation.seq.util.Action;
import org.simulation.seq.util.CarPercept;
import org.simulation.seq.util.MoveForward;
import org.simulation.seq.util.TrafficLightInfo;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CarAgentActor extends AbstractActor {

    /* abstract agent */
    private String myId;
    //TODO: private Environment env; // non dovrebbe servire perchè l'env è unico => attore

    /* Car Agent */
    protected double maxSpeed;
    protected double currentSpeed;
    protected double acceleration;
    protected double deceleration;
    protected CarPercept currentPercept;
    protected Optional<Action> selectedAction;

    /* CAREXTENDED */
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

    public CarAgentActor(String id, Road road, double initialPos, double acc, double dec, double vmax) {
        super();
        this.myId = id;
        this.acceleration = acc;
        this.deceleration = dec;
        this.maxSpeed = vmax;
        getContext().actorSelection("/user/env").tell(new Message<>("register-car", List.of(id, this, road, initialPos)), ActorRef.noSender());
        state = CarAgentState.STOPPED;
    }

    private String getId() {
        return myId;
    }

    private void step(int dt) {
        System.out.println("Step for car " + getId() + "...");

        Future<Object> future = Patterns.ask(getContext().actorSelection("/user/env"), new Message<>("get-current-percepts", List.of(getId())), 1000);
        try {
            currentPercept = (CarPercept) Await.result(future, Duration.create(5, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        /* decide */
        selectedAction = Optional.empty();

        decide(dt);

        /* act */
        if (selectedAction.isPresent()) {
            getContext().actorSelection("/user/env").tell(new Message<>("submit-action", List.of(selectedAction.get())), ActorRef.noSender());
        }
    }

    private double getCurrentSpeed() {
        return currentSpeed;
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
            selectedAction = Optional.of(new MoveForward(getId(), 0));
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

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "get-id".equals(message.name()), message -> getSender().tell(getId(), getSelf()))
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.contents().get(1)))
                .match(Message.class, message -> "get-current-speed".equals(message.name()), message -> getSender().tell(getCurrentSpeed(), getSelf()))
                .match(Message.class, message -> "stop".equals(message.name()), s -> getContext().stop(self()))
                .build();
    }
}
