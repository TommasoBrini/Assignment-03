package pcd.ass01.agent.implementation;

import pcd.ass01.agent.implementation.CarAgent;
import pcd.ass01.environment.implementation.Road;

public  class CarAgentInfo {

	private CarAgent car;
	private double pos;
	private Road road;
	
	public CarAgentInfo(CarAgent car, Road road, double pos) {
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
	
	public CarAgent getCar() {
		return car;
	}	
	
	public Road getRoad() {
		return road;
	}
}
