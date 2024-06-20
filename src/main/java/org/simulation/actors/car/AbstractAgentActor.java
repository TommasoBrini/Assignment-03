package org.simulation.actors.car;


import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import org.simulation.actors.Message;

public abstract class AbstractAgentActor extends AbstractActor {
    private final ActorSelection target;

    private String myId;

    public AbstractAgentActor(String id) {
        super();
        this.myId = id;
        this.target = getContext().actorSelection("/user/counter");
    }

    protected void step(int dt) {
    }

    public String getId() {
        return myId;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.content()))
                .match(Message.class, message -> "stop".equals(message.name()), s -> getContext().stop(self()))
                .build();
    }
}
