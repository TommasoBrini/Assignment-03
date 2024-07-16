package org.ass03.part1.model.environment;

import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import org.ass03.part1.util.Message;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements SimulationListener {

	private double averageSpeed;
	private double minSpeed;
	private double maxSpeed;
	private final int nCars;

	public RoadSimStatistics(int n) {
		this.nCars = n;
	}
	
	@Override
	public void notifyInit(int t) {
		// TODO Auto-generated method stub
		// log("reset: " + t);
		averageSpeed = 0;
	}

	@Override
	public void notifyStepDone(int t, ActorSystem system) {
		double avSpeed = 0;
		
		maxSpeed = -1;
		minSpeed = Double.MAX_VALUE;

		for(var i = 0; i < nCars; i++){
			Future<Object> future = Patterns.ask(system.actorSelection("/user/car*"), new Message("get-current-speed", List.of()), 1000);
			try {
				double currSpeed = (double) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
				avSpeed += currSpeed;
				if (currSpeed > maxSpeed) {
					maxSpeed = currSpeed;
				} else if (currSpeed < minSpeed) {
					minSpeed = currSpeed;
				}
			} catch (TimeoutException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		if (nCars > 0) {
			avSpeed /= nCars;
		}
		log("average speed: " + avSpeed);
	}
	
	public double getAverageSpeed() {
		return this.averageSpeed;
	}

	public double getMinSpeed() {
		return this.minSpeed;
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	
	private void log(String msg) {
		System.out.println("[STAT] " + msg);
	}

}
