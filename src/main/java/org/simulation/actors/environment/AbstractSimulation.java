package org.simulation.actors.environment;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import org.simulation.actors.util.Message;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractSimulation {
    protected ActorSystem system;

    /* logical time step */
    private int dt;

    /* initial logical time */
    private int t0;

    protected int numCars;

    protected AbstractSimulation(int numCar){
        system = ActorSystem.create("TrafficSimulation");
        system.actorOf(Props.create(EnvironmentActor.class, "RoadEnv"), "env");
        system.actorOf(Props.create(GUIActor.class), "gui");
        this.numCars = numCar;
    }

    /**
     *
     * Method used to configure the simulation, specifying env and agents
     *
     */
    protected abstract void setup();

    /**
     * Method running the simulation for a number of steps,
     * using a sequential approach
     *
     * @param numSteps
     */
    public void run(int numSteps) {

        system.actorSelection("/user/gui").tell(new Message<>("set-start", List.of(numSteps)), ActorRef.noSender());

        /* initialize the env and the agents inside */
        int t = t0;

        system.actorSelection("/user/env").tell(new Message<>("init", List.of(numSteps, numCars)), ActorRef.noSender());
        for (int i = 0; i < 4; i++) {
            system.actorSelection("/user/car-" + i).tell(new Message<>("init", null), ActorRef.noSender());
        }

        this.notifyReset(t);

        /* make a step */
        system.actorSelection("/user/env").tell(new Message<>("step", List.of(dt)), ActorRef.noSender());
    }

    public long getSimulationDuration() {
        long dur;
        Future<Object> future = Patterns.ask(system.actorSelection("/user/gui"), new Message<>("get-duration", List.of()), 1000);
        try {
            dur = (long) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return dur;
    }

    public long getAverageTimePerCycle() {
        long avg;
        Future<Object> future = Patterns.ask(system.actorSelection("/user/gui"), new Message<>("get-average-time-per-cycle", List.of()), 1000);
        try {
            avg = (long) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        system.actorSelection("/user/gui").tell(new Message<>("get-average-time-per-cycle", List.of()), ActorRef.noSender());
        return avg;
    }

    /* methods for configuring the simulation */

    protected void setupTimings(int t0, int dt) {
        this.dt = dt;
        this.t0 = t0;
    }

    protected void syncWithTime(int nCyclesPerSec) {
        system.actorSelection("/user/gui").tell(new Message<>("sync-with-time", List.of(nCyclesPerSec)), ActorRef.noSender());
    }

    /* methods for listeners */

    public void addSimulationListener(SimulationListener l) {
        system.actorSelection("/user/gui").tell(new Message<>("add-listener", List.of(l)), ActorRef.noSender());
    }

    private void notifyReset(int t0) {
        system.actorSelection("/user/gui").tell(new Message<>("reset", List.of(t0)), ActorRef.noSender());
    }

    public int getNumCars() {
        return numCars;
    }

    public boolean isCompleted(){
        Future<Object> future = Patterns.ask(system.actorSelection("/user/env"), new Message<>("is-completed", List.of()), 1000);
        try {
            return (boolean) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        system.terminate();
    }
}
