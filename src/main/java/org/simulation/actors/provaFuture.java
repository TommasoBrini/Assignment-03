package org.simulation.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import org.simulation.actors.car.CarAgentActor;
import org.simulation.actors.environment.EnvironmentActor;
import org.simulation.actors.environment.Road;
import org.simulation.actors.util.Message;
import org.simulation.actors.util.P2d;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class provaFuture {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("TrafficSimulation");
        system.actorOf(Props.create(EnvironmentActor.class, "RoadEnv"), "env");
        /*system.actorSelection("/user/env").tell(new Message<>("create-road", List.of(new P2d(0, 300), new P2d(1500, 300))), ActorRef.noSender());
        System.out.println("creato env");*/
        Future<Object> future = Patterns.ask(system.actorSelection("/user/env"), new Message<>("create-road", List.of(new P2d(0, 300), new P2d(1500, 300))), 1000);
        try {
            Road r = (Road) Await.result(future, Duration.create(5, TimeUnit.SECONDS));
            System.out.println("Road: " + r.getFrom());
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        system.terminate();
    }
}
