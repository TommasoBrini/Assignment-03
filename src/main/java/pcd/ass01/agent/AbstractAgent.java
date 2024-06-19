package pcd.ass01.agent;

import pcd.ass01.environment.AbstractEnvironment;

/**
 * 
 * Base  class for defining types of agents taking part to the simulation
 * 
 */
public abstract class AbstractAgent {
	
	private String myId;
	private AbstractEnvironment env;
	private int dt;

	/**
	 * Each agent has an identifier
	 * 
	 * @param id
	 */
	protected AbstractAgent(String id) {
		this.myId = id;
	}
	
	/**
	 * This method is called at the beginning of the simulation
	 * 
	 * @param env
	 */
	public void init(AbstractEnvironment env) {
		this.env = env;
	}
	
	/**
	 * This method is called at each step of the simulation
	 * 
	 * @param dt - logical time step
	 */
	abstract public void step(int dt);

	public void setDt(int dt) {
		this.dt = dt;
	}

	public String getAgentId() {
		return myId;
	}
	
	protected AbstractEnvironment getEnv() {
		return this.env;
	}
}
