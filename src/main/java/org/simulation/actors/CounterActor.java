package org.simulation.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;

public class CounterActor extends AbstractActor {
    private int count = 0;
    private final int numCar;
    private final ActorSelection target;

    public CounterActor(int numCar) {
        super();
        this.numCar = numCar;
        this.target = getContext().actorSelection("/user/env");
    }

    private void inc() {
        this.count++;
        if(count == numCar) {
            System.out.println("All cars have finished their steps");
            System.out.println("Count: " + count);
            this.count = 0;
            this.target.tell(new Message<String>("step", ""), getSelf());
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("inc", s -> inc())
                .matchEquals("stop", s -> getContext().stop(self())).build();

    }
}
