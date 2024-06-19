package org.simulation.actors;

import akka.actor.*;

public class EnvironmentActor extends AbstractActor {

    public static record Message(int numCar) {}
    private int actualNumStep;
    private final int maxNumStep;

    public EnvironmentActor(int numCar, int maxNumStep) {
        super();
        this.actualNumStep = 1;
        this.maxNumStep = maxNumStep;
        for(int i = 0; i < numCar; i++) {
            getContext().actorOf(Props.create(CarActor.class), "car-" + i);
        }
    }

    private void doStep() {
        if (this.actualNumStep == this.maxNumStep + 1) {
            getContext().actorSelection("/user/*").tell("stop", ActorRef.noSender());
            getContext().system().terminate();
            return;
        }
        System.out.println("Env Step " + this.actualNumStep++);
        getContext().actorSelection("/user/env/*").tell("step", ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("step", s -> doStep()).build();
    }

}
