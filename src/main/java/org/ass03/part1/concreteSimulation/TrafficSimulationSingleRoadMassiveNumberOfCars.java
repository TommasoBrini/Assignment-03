package org.ass03.part1.concreteSimulation;

import akka.actor.Props;
import akka.pattern.Patterns;
import org.ass03.part1.car.CarAgentActor;
import org.ass03.part1.environment.AbstractSimulation;
import org.ass03.part1.environment.Road;
import org.ass03.part1.util.Message;
import org.ass03.part1.util.P2d;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TrafficSimulationSingleRoadMassiveNumberOfCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadMassiveNumberOfCars(int numCars) {
		super(numCars);
	}
	
	public void setup() {
		this.setupTimings(0, 1);

		Road r;
		Future<Object> future = Patterns.ask(system.actorSelection("/user/env"), new Message("create-road", List.of(new P2d(0, 300), new P2d(1500, 300))), 1000);
		try {
			r = (Road) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		for (int i = 0; i < this.numCars; i++) {
			
			String carId = "car-" + i;
			double initialPos = i*10;			
			double carAcceleration = 1; //  + gen.nextDouble()/2;
			double carDeceleration = 0.3; //  + gen.nextDouble()/2;
			double carMaxSpeed = 7; // 4 + gen.nextDouble();
			system.actorOf(Props.create(CarAgentActor.class, carId, r, initialPos, carAcceleration, carDeceleration, carMaxSpeed), carId);

		}
		
	}	
}
	