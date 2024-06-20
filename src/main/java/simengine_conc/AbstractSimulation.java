package simengine_conc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public abstract class AbstractSimulation {

	private AbstractEnvironment env;
	private List<AbstractAgent> agents;
	private List<SimulationListener> listeners;

	private int dt;
	private int t0;

	private int nSteps;
	private int nWorkers;
	
	/* for time statistics*/
	private long startWallTime;
	private long endWallTime;

	protected AbstractSimulation() {
		agents = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	public void configureNumWorkers(int nWorkers) {
		this.nWorkers = nWorkers;
	}
	
	public abstract void setup();
	
	public void run(int nSteps, Flag stopFlag, boolean syncWithTime) {
		this.nSteps = nSteps;

		startWallTime = System.currentTimeMillis();
		
		Semaphore done = new Semaphore(0);
		MasterAgent agent = new MasterAgent(this, nWorkers, nSteps, stopFlag, done, syncWithTime);
		agent.start();
		
		try {
			done.acquire();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		endWallTime = System.currentTimeMillis();
		
	}
	
	protected void setTimings(int t0, int dt) {
		this.dt = dt;
		this.t0 = t0;
	}
			
	public void setEnvironment(AbstractEnvironment env) {
		this.env = env;
	}
		
	public AbstractEnvironment getEnvironment() {
		return env;
	}

	public void addAgent(AbstractAgent agent) {
		agents.add(agent);
		env.registerNewAgent(agent);
	}
	
	public  List<AbstractAgent> getAgents(){
		return agents;
	}
	
	public int getInitialTime() {
		return t0;
	}
	
	public int getTimeStep() {
		return dt;
	}
	
	public int getNumSteps() {
		return nSteps;
	}
		
	public void startedAt(long t) {
		this.startWallTime = t;
	}
	
	public void completedAt(long t) {
		this.endWallTime = t;
	}

	public long getSimulationDuration() {
		return endWallTime - startWallTime;
	}
	
	public long getAverageTimePerStep() {
		return getSimulationDuration()/nSteps;
	}

	
	public void addSimulationListener(SimulationListener l) {
		this.listeners.add(l);
	}
	
	public List<SimulationListener> getListeners(){
		return this.listeners;
	}
	
	public void notifyReset(int t0, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyInit(t0, agents, env);
		}
	}

	public void notifyNewStep(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyStepDone(t, agents, env);
		}
	}
	
	
}
