package org.simulation.actors.environment;

import akka.actor.AbstractActor;
import org.simulation.actors.Message;
import org.simulation.actors.util.Action;
import org.simulation.actors.util.Percept;

import java.util.List;

public abstract class AbstractEnvironmentActor extends AbstractActor {
    private String id;
    protected List<Action> submittedActions;

    public AbstractEnvironmentActor(String id) {
        super();
        this.id = id;
        // TODO: create car actor ???
    }

    /**
     *
     * Called at the beginning of the simulation
     */
    protected abstract void init();

    /**
     *
     * Called at each step of the simulation
     *
     * @param dt
     */
    protected abstract void step(int dt);

    /**
     *
     * Called by an agent to get its percepts
     *
     * @param agentId - identifier of the agent
     * @return agent percept
     */
    protected abstract Percept getCurrentPercepts(String agentId);

    /**
     *
     * Called by agent to submit an action to the environment
     *
     * @param act - the action
     */
    private void submitAction(Action act) {
        submittedActions.add(act);
    }

    /**
     *
     * Called at each simulation step to clean the list of actions
     * submitted by agents
     *
     */
    private void cleanActions() {
        submittedActions.clear();
    }

    /**
     *
     * Called at each simulation step to process the actions
     * submitted by agents.
     *
     */
    protected abstract void processActions();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "get-id".equals(message.name()), message -> getSelf().tell(id, getSelf()))
                .match(Message.class, message -> "init".equals(message.name()), message -> init())
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.content()))
                .match(Message.class, message -> "get-current-percepts".equals(message.name()), message -> getSender().tell(getCurrentPercepts((String) message.content()), getSelf()))
                .match(Message.class, message -> "submit-action".equals(message.name()), message -> submitAction((Action) message.content()))
                .match(Message.class, message -> "clean-actions".equals(message.name()), message -> cleanActions())
                .match(Message.class, message -> "process-actions".equals(message.name()), message -> processActions())
                .build();
    }

}
