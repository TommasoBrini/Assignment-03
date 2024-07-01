package org.simulation.part1.concreteSimulation;

import akka.actor.Props;
import akka.pattern.Patterns;
import org.simulation.part1.car.CarAgentActor;
import org.simulation.part1.environment.Road;
import org.simulation.part1.util.Message;
import org.simulation.part1.environment.AbstractSimulation;
import org.simulation.part1.util.P2d;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

    public TrafficSimulationSingleRoadTwoCars() {
        super(2);
    }

    public void setup() {
        int t0 = 0;
        int dt = 1;

        Road r;
        Future<Object> future = Patterns.ask(system.actorSelection("/user/env"), new Message("create-road", List.of(new P2d(0, 300), new P2d(1500, 300))), 1000);
        try {
            r = (Road) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        system.actorOf(Props.create(CarAgentActor.class, "car-1", r, 0.0, 0.1, 0.2, 8.0), "car-1");
        System.out.println("creato car-1");

        system.actorOf(Props.create(CarAgentActor.class, "car-2", r, 100.0, 0.1, 0.1, 7.0), "car-2");
        System.out.println("creato car-2");

        this.setupTimings(t0, dt);

        /* sync with wall-time: 25 steps per sec */
        this.syncWithTime(25);
    }
}
