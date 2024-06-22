package org.simulation.actors.concreteSimulation;

import akka.actor.ActorRef;
import org.simulation.actors.Message;
import org.simulation.actors.environment.AbstractSimulation;
import org.simulation.seq.car.CarAgent;
import org.simulation.seq.car.CarAgentBasic;
import org.simulation.seq.environment.Road;
import org.simulation.seq.environment.RoadsEnv;
import org.simulation.seq.util.P2d;

import java.util.List;

public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

    public TrafficSimulationSingleRoadTwoCars(int numcar) {
        super(numcar);
    }

    public void setup() {
        int t0 = 0;
        int dt = 1;

        this.system.actorSelection("/user/env").tell(new Message<>("create-road", List.of(new P2d(0, 300), new P2d(1500, 300))), ActorRef.noSender());

        this.setupTimings(t0, dt);

        /* sync with wall-time: 25 steps per sec */
        this.syncWithTime(25);
    }
}
