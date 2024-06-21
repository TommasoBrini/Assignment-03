package org.simulation.actors.environment;

import akka.actor.AbstractActor;
import org.simulation.actors.Message;
import org.simulation.actors.car.CarAgentInfo;
import org.simulation.actors.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EnvironmentActor extends AbstractActor {
    private static final int MIN_DIST_ALLOWED = 5;
    private static final int CAR_DETECTION_RANGE = 30;
    private static final int SEM_DETECTION_RANGE = 30;
    private String id;
    protected List<Action> submittedActions;
    private List<Road> roads;

    /* traffic lights */
    private List<TrafficLight> trafficLights;

    /* cars situated in the environment */
    private HashMap<String, CarAgentInfo> registeredCars;

    public EnvironmentActor(String id) {
        super();
        this.id = id;
        registeredCars = new HashMap<>();
        trafficLights = new ArrayList<>();
        roads = new ArrayList<>();
        // TODO: create car actor ???
    }

    /**
     *
     * Called at the beginning of the simulation
     */
    public void init() {
        for (var tl: trafficLights) {
            tl.init();
        }
    }
    /**
     *
     * Called at each step of the simulation
     *
     * @param dt
     */
    public void step(int dt) {
        System.out.println("ENV STEP");
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

    /**
     *
     * Called by an agent to get its percepts
     *
     * @param agentId - identifier of the agent
     * @return agent percept
     */
    public Percept getCurrentPercepts(String agentId) {

        CarAgentInfo carInfo = registeredCars.get(agentId);
        double pos = carInfo.getPos();
        Road road = carInfo.getRoad();
        Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
        Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);

        return new CarPercept(pos, nearestCar, nearestSem);
    }

    private Optional<CarAgentInfo> getNearestCarInFront(Road road, double carPos, double range){
        return
                registeredCars
                        .entrySet()
                        .stream()
                        .map(el -> el.getValue())
                        .filter((carInfo) -> carInfo.getRoad() == road)
                        .filter((carInfo) -> {
                            double dist = carInfo.getPos() - carPos;
                            return dist > 0 && dist <= range;
                        })
                        .min((c1, c2) -> (int) Math.round(c1.getPos() - c2.getPos()));
    }

    private Optional<TrafficLightInfo> getNearestSemaphoreInFront(Road road, double carPos, double range){
        return
                road.getTrafficLights()
                        .stream()
                        .filter((TrafficLightInfo tl) -> tl.roadPos() > carPos)
                        .min((c1, c2) -> (int) Math.round(c1.roadPos() - c2.roadPos()));
    }

    /**
     *
     * Called by agent to submit an action to the environment
     *
     * @param act - the action
     */
    private void submitAction(Action act) {
        submittedActions.add(act);
    }

    /**
     *
     * Called at each simulation step to clean the list of actions
     * submitted by agents
     *
     */
    private void cleanActions() {
        submittedActions.clear();
    }

    /**
     *
     * Called at each simulation step to process the actions
     * submitted by agents.
     *
     */
    public void processActions() {
        for (var act: submittedActions) {
            if (act instanceof MoveForward) {
                MoveForward mv = (MoveForward) act;
                CarAgentInfo info = registeredCars.get(mv.agentId());
                Road road = info.getRoad();
                Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road, info.getPos(), CAR_DETECTION_RANGE);
                if (!nearestCar.isEmpty()) {
                    double dist = nearestCar.get().getPos() - info.getPos();
                    if (dist > mv.distance() + MIN_DIST_ALLOWED) {
                        info.updatePos(info.getPos() + mv.distance());
                    }
                } else {
                    info.updatePos(info.getPos() + mv.distance());
                }

                if (info.getPos() > road.getLen()) {
                    info.updatePos(0);
                }
            }
        }
    }

    public List<CarAgentInfo> getAgentInfo(){
        return this.registeredCars.entrySet().stream().map(el -> el.getValue()).toList();
    }

    public List<Road> getRoads(){
        return roads;
    }

    public List<TrafficLight> getTrafficLights(){
        return trafficLights;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "get-id".equals(message.name()), message -> getSelf().tell(id, getSelf()))
                .match(Message.class, message -> "init".equals(message.name()), message -> init())
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.content()))
                .match(Message.class, message -> "get-current-percepts".equals(message.name()), message -> getSender().tell(getCurrentPercepts((String) message.content()), getSelf()))
                .match(Message.class, message -> "submit-action".equals(message.name()), message -> submitAction((Action) message.content()))
                .match(Message.class, message -> "clean-actions".equals(message.name()), message -> cleanActions())
                .match(Message.class, message -> "process-actions".equals(message.name()), message -> processActions())
                .build();
    }

}
