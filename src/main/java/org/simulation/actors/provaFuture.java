package org.simulation.actors;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import org.simulation.actors.car.CarAgentActor;
import org.simulation.actors.environment.Road;
import org.simulation.actors.util.P2d;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class provaFuture {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("TrafficSimulation");
        system.actorOf(Props.create(CarAgentActor.class, "car-1", new Road(new P2d(1,1), new P2d(2,2)), 0.0, 0.0, 0.0), "car-1");
        Future<Object> future = Patterns.ask(system.actorSelection("/user/car-1"), new Message<>("get-id", null), 1000);
        try {
            String id = (String) Await.result(future, Duration.create(5, TimeUnit.SECONDS));
            System.out.println("ID: " + id);
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        system.terminate();
    }
}
