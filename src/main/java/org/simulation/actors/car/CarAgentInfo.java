package org.simulation.actors.car;

import org.simulation.actors.environment.Road;

public  class CarAgentInfo {

	private CarAgentActor car;
	private double pos;
	private Road road;
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
