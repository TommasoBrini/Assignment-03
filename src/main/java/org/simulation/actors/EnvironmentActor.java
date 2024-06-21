package org.simulation.actors;

import akka.actor.*;
import org.simulation.actors.car.CarAgentActor;

public class EnvironmentActor extends AbstractActor {
    private int actualNumStep;
    private final int maxNumStep;

    public EnvironmentActor(int numCar, int maxNumStep) {
        super();
        this.actualNumStep = 1;
        this.maxNumStep = maxNumStep;
        for(int i = 0; i < numCar; i++) {
            getContext().actorOf(Props.create(CarAgentActor.class), "car-" + i);
        }
    }

    private void step(int id) {
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
        return receiveBuilder()
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.content()))
                .match(Message.class, message -> "prova".equals(message.name()), message -> doProva())
                .build();
    }

    private void doProva() {
        System.out.println("Prova");
    }

}
