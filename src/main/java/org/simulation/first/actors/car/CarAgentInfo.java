package org.simulation.first.actors.car;

import org.simulation.first.actors.environment.Road;

public  class CarAgentInfo {

	private final CarAgentActor car;
	private double pos;
	private final Road road;
	//TODO: gestire le info delle car, non avendo più carAgent ma actor
	
	public CarAgentInfo(CarAgentActor car, Road road, double pos) {
		this.car = car;
		this.road = road;
		this.pos = pos;
	}
	
	public double getPos() {
		return pos;
	}
	
	public void updatePos(double pos) {
		this.pos = pos;
	}

	public CarAgentActor getCar() {
		return car;
	}
	
	public Road getRoad() {
		return road;
	}
}
