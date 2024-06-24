package org.simulation.actors.environment;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.simulation.actors.util.Message;

import java.util.ArrayList;
import java.util.List;

public class GUIActor extends AbstractActor {

    private List<SimulationListener> listeners;

    /* in the case of sync with wall-time */
    private boolean toBeInSyncWithWallTime;
    private int nStepsPerSec;

    /* for time statistics*/
    private long currentWallTime;
    private long startWallTime;
    private long endWallTime;
    private long averageTimePerStep;
    private long timePerStep;
    private int nSteps;

    public GUIActor(){
        super();
        this.listeners = new ArrayList<>();
        this.toBeInSyncWithWallTime = false;
        this.timePerStep = 0;
    }

    private void addSimulationListener(SimulationListener listener){
        System.out.println("Adding listener" + listener.getClass().getName());
        listeners.add(listener);
    }

    private void setCurrentWallTime(){
        currentWallTime = System.currentTimeMillis();
    }

    private void setStartWallTime(int nSteps){
        this.nSteps = nSteps;
        startWallTime = System.currentTimeMillis();
    }

    private void setEndWallTime(){
        endWallTime = System.currentTimeMillis();
        this.averageTimePerStep = timePerStep / nSteps;
    }

    private void notifyReset(int t0){
        System.out.println("Resetting simulation");
        for (var listener: listeners){
            listener.notifyInit(t0);
        }
    }

    private void notifyNewStep(int t, ActorSystem system){
        timePerStep += System.currentTimeMillis() - currentWallTime;
        if (toBeInSyncWithWallTime) {
            syncWithWallTime();
        }
        for (var l: listeners) {
            l.notifyStepDone(t, system);
        }
        System.out.println("notify new step ");
        getContext().actorSelection("/user/env").tell(new Message<>("step", List.of(t)), ActorRef.noSender());
    }

    private void syncWithWallTime() {
        try {
            long newWallTime = System.currentTimeMillis();
            long delay = 1000 / this.nStepsPerSec;
            long wallTimeDT = newWallTime - currentWallTime;
            if (wallTimeDT < delay) {
                Thread.sleep(delay - wallTimeDT);
            }
        } catch (Exception ex) {}
    }

    private long getSimulationDuration(){
        return endWallTime - startWallTime;
    }

    protected void syncWithTime(int nCyclesPerSec) {
        this.toBeInSyncWithWallTime = true;
        this.nStepsPerSec = nCyclesPerSec;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "set-start".equals(message.name()), message -> setStartWallTime((Integer) message.contents().get(0)))
                .match(Message.class, message -> "set-end".equals(message.name()), message -> setEndWallTime())
                .match(Message.class, message -> "add-listener".equals(message.name()), message -> addSimulationListener((SimulationListener) message.contents().get(0)))
                .match(Message.class, message -> "reset".equals(message.name()), message -> notifyReset((Integer) message.contents().get(0)))
                .match(Message.class, message -> "new-step".equals(message.name()), message -> notifyNewStep((Integer) message.contents().get(0), (ActorSystem) message.contents().get(1)))
                .match(Message.class, message -> "sync".equals(message.name()), message -> syncWithWallTime())
                .match(Message.class, message -> "get-duration".equals(message.name()), message -> getSender().tell(getSimulationDuration(), getSelf()))
                .match(Message.class, message -> "sync-with-time".equals(message.name()), message -> syncWithTime((Integer) message.contents().get(0)))
                .match(Message.class, message -> "get-average-time-per-cycle".equals(message.name()), message -> getSender().tell(averageTimePerStep, getSelf()))
                .match(Message.class, message -> "set-current".equals(message.name()), message -> setCurrentWallTime())
                .build();
    }
}
