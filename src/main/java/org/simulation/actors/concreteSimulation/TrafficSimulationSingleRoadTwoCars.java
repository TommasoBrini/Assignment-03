package org.simulation.actors.concreteSimulation;

import org.simulation.actors.environment.AbstractSimulation;
import org.simulation.seq.car.CarAgent;
import org.simulation.seq.car.CarAgentBasic;
import org.simulation.seq.environment.Road;
import org.simulation.seq.environment.RoadsEnv;
import org.simulation.seq.util.P2d;

public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

    public TrafficSimulationSingleRoadTwoCars(int numcar) {
        super(numcar);
    }

    public void setup() {

        int t0 = 0;
        int dt = 1;

        this.setupTimings(t0, dt);

        RoadsEnv env = new RoadsEnv();

        /* sync with wall-time: 25 steps per sec */
        this.syncWithTime(25);
    }
}
