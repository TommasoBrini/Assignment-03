package org.simulation.actors.environment;

import akka.actor.ActorSystem;
import akka.actor.Props;
import org.simulation.actors.car.CarAgentActor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSimulation {
    //TODO: env & agents ???
    private ActorSystem system;
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

    protected AbstractSimulation() {
        system = ActorSystem.create("TrafficSimulation");
        //agents = new ArrayList<>();
        system.actorOf(Props.create(EnvironmentActor.class), "env");
        System.out.println("creato env");
        int numCar = 4;
        for(int i = 0; i < numCar; i++) {
            system.actorOf(Props.create(CarAgentActor.class), "car-" + i);
            System.out.println("creato car-" + i);
        }
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

        system.actorSelection("env").tell("init", null);
        for (int i = 0; i < 4; i++) {
            system.actorSelection("car-" + i).tell("init", null);
        }

        this.notifyReset(t);

        long timePerStep = 0;
        int nSteps = 0;

        while (nSteps < numSteps) {

            currentWallTime = System.currentTimeMillis();

            /* make a step */

            system.actorSelection("env").tell("step", null);

            /* clean the submitted actions */
            system.actorSelection("env").tell("cleanActions", null);

            /* ask each agent to make a step */

            for(int i = 0; i < 4; i++) {
                system.actorSelection("car-" + i).tell("step", null);
            }
            t += dt;

            /* process actions submitted to the environment */

            system.actorSelection("env").tell("processActions", null);

            notifyNewStep(t);

            nSteps++;
            timePerStep += System.currentTimeMillis() - currentWallTime;

            if (toBeInSyncWithWallTime) {
                syncWithWallTime();
            }
        }

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
