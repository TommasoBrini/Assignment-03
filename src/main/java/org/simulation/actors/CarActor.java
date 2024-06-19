package org.simulation.actors;


import akka.actor.AbstractActor;
import akka.actor.ActorSelection;

public class CarActor extends AbstractActor {

    private final ActorSelection target;

    public CarActor() {
        super();
        this.target = getContext().actorSelection("/user/counter");
    }

    public void step() {
        System.out.println("[Car " + this.self().path() + "]: step");
        this.target.tell("inc", getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("step", s -> {
            step();
        }).matchEquals("stop", s -> getContext().stop(self())).build();
    }
}
