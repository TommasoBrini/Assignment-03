package simtraffic_conc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import simengine_conc.*;
import simengine_conc.AbstractEnvironment;
import simengine_conc.Percept;

/**
 * 
 * Environment modelling some roads, with traffic lights.
 * 
 */
public class RoadsEnv extends AbstractEnvironment {
		
	public static final int MIN_DIST_ALLOWED = 5;
	public static final int CAR_DETECTION_RANGE = 30;
	public static final int SEM_DETECTION_RANGE = 30;
	
	/* list of roads */
	private List<Road> roads;

	/* traffic lights */
	private List<TrafficLight> trafficLights;
	

	public RoadsEnv() {
		super("traffic-env");
		trafficLights = new ArrayList<>();
		roads = new ArrayList<>();
	}
	
	public void init() {
		for (var tl: trafficLights) {
			tl.init();
		}
	}
	
	public void step(int dt) {
		for (var tl: trafficLights) {
			tl.step(dt);
		}
	}

	public Road createRoad(P2d p0, P2d p1) {
		Road r = new Road(p0, p1);
		this.roads.add(r);
		return r;
	}

	public TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
		this.trafficLights.add(tl);
		return tl;
	}

	@Override
	public Percept getCurrentPercepts(String agentId) {
		
		AbstractCar car = (AbstractCar) getAgent(agentId);
		double pos = car.getPos();
		Road road = car.getRoad();
		Optional<AbstractCar> nearestCar = getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
		Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);
		
		return new CarPercept(pos, nearestCar, nearestSem);
	}

	public Optional<AbstractCar> getNearestCarInFront(Road road, double carPos, double range){
		var opt =  this.getRegisteredAgents()
				.filter((car) -> ((AbstractCar)car).getRoad() == road)				
				.filter((car) -> {
					double dist = ((AbstractCar)car).getPos() - carPos;
					return dist > 0 && dist <= range;
				})
				.min((c1, c2) -> (int) Math.round(((AbstractCar)c1).getPos() - ((AbstractCar)c2).getPos()));
		return opt.isEmpty() ? Optional.empty() : Optional.of((AbstractCar)opt.get());
	}

	public Optional<TrafficLightInfo> getNearestSemaphoreInFront(Road road, double carPos, double range){
		return 
				road.getTrafficLights()
				.stream()
				.filter((TrafficLightInfo tl) -> tl.roadPos() > carPos)
				.min((c1, c2) -> (int) Math.round(c1.roadPos() - c2.roadPos()));
	}
		
	public List<Road> getRoads(){
		return roads;
	}
	
	public List<TrafficLight> getTrafficLights(){
		return trafficLights;
	}
	
	public List<AbstractCar> getCars(){
		var l = new ArrayList<AbstractCar>();
		this.getRegisteredAgents().forEach(ag -> {
			l.add((AbstractCar) ag);
		});
		return l;
	}

}
