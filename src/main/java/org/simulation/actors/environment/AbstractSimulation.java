package org.simulation.actors.environment;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.simulation.actors.util.Message;
import org.simulation.actors.car.CarAgentActor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSimulation {
    protected ActorSystem system;
    /* simulation listeners */
    private List<SimulationListener> listeners;

    /* logical time step */
    private int dt;

    /* initial logical time */
    private int t0;

    /* in the case of sync with wall-time */
    private boolean toBeInSyncWithWallTime;
    private int nStepsPerSec;

    /* for time statistics*/
    private long currentWallTime;
    private long startWallTime;
    private long endWallTime;
    private long averageTimePerStep;
    protected int numCar;

    protected AbstractSimulation(int numCar){
        system = ActorSystem.create("TrafficSimulation");
        system.actorOf(Props.create(EnvironmentActor.class, "RoadEnv"), "env");
        System.out.println("creato env");
        this.numCar = numCar;
        listeners = new ArrayList<>();
        toBeInSyncWithWallTime = false;
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

        startWallTime = System.currentTimeMillis();

        /* initialize the env and the agents inside */
        int t = t0;

        system.actorSelection("/user/env").tell(new Message<>("init", List.of(numSteps, numCar)), ActorRef.noSender());
        for (int i = 0; i < 4; i++) {
            system.actorSelection("/user/car-" + i).tell(new Message<>("init", null), ActorRef.noSender());
        }

        this.notifyReset(t);

        long timePerStep = 0;

        currentWallTime = System.currentTimeMillis();

        System.out.println("Step: " + t);
        /* make a step */
        system.actorSelection("/user/env").tell(new Message<>("step", List.of(dt)), ActorRef.noSender());

        //TODO: stats for the simulation
        /*
        t += dt;

        /* process actions submitted to the environment

        system.actorSelection("/user/env").tell(new Message<>("process-actions", null), ActorRef.noSender());

        notifyNewStep(t);

        nSteps++;
        timePerStep += System.currentTimeMillis() - currentWallTime;

        if (toBeInSyncWithWallTime) {
            syncWithWallTime();
        }*/

        endWallTime = System.currentTimeMillis();
        this.averageTimePerStep = timePerStep / numSteps;

    }

    public long getSimulationDuration() {
        return endWallTime - startWallTime;
    }

    public long getAverageTimePerCycle() {
        return averageTimePerStep;
    }

    /* methods for configuring the simulation */

    protected void setupTimings(int t0, int dt) {
        this.dt = dt;
        this.t0 = t0;
    }

    protected void syncWithTime(int nCyclesPerSec) {
        this.toBeInSyncWithWallTime = true;
        this.nStepsPerSec = nCyclesPerSec;
    }

    /* methods for listeners */

    public void addSimulationListener(SimulationListener l) {
        this.listeners.add(l);
    }

    private void notifyReset(int t0) {
        for (var l: listeners) {
            l.notifyInit(t0);
        }
    }

    private void notifyNewStep(int t) {
        for (var l: listeners) {
            l.notifyStepDone(t);
        }
    }

    /* method to sync with wall time at a specified step rate */

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


}
