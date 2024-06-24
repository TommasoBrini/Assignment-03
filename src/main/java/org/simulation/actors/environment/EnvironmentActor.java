package org.simulation.actors.environment;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import org.simulation.actors.util.Message;
import org.simulation.actors.car.CarAgentActor;
import org.simulation.actors.car.CarAgentInfo;
import org.simulation.actors.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EnvironmentActor extends AbstractActor {
    /* abstact environment */
    private String id;
    protected List<Action> submittedActions;
    private static final int MIN_DIST_ALLOWED = 5;
    private static final int CAR_DETECTION_RANGE = 30;
    private static final int SEM_DETECTION_RANGE = 30;
    private int dt;
    private List<Road> roads;
    private int nstep;
    private int nCars;
    private int count = 0;

    /* traffic lights */
    private List<TrafficLight> trafficLights;
    private int actualNumStep = 0;

    /* cars situated in the environment */
    private HashMap<String, CarAgentInfo> registeredCars;

    public EnvironmentActor(String id) {
        super();
        this.id = id;
        submittedActions = new ArrayList<>();
        registeredCars = new HashMap<>();
        trafficLights = new ArrayList<>();
        roads = new ArrayList<>();
    }

    private String getId() {
        return id;
    }

    /**
     *
     * Called at the beginning of the simulation
     */
    private void init(int step, int nCars) {
        this.nstep = step;
        this.nCars = nCars;
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
    private void step(int dt) {
        this.dt = dt;
        getContext().actorSelection("/user/gui").tell(new Message<>("set-current", List.of()), ActorRef.noSender());
        if (this.actualNumStep == this.nstep) {
            getContext().actorSelection("/user/gui").tell(new Message<>("set-end", null), ActorRef.noSender());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getContext().system().terminate();
            return;
        }
        System.out.println("ENV STEP " + this.actualNumStep + "... max step: " + this.nstep);
        for (var tl: trafficLights) {
            tl.step(dt);
        }
        this.actualNumStep++;

        /* clean actions */
        submittedActions.clear();

        /* ask each agent to make a step */
        getContext().actorSelection("/user/car-*").tell(new Message<>("step", List.of(dt)), ActorRef.noSender());
    }

    /**
     *
     * Called by an agent to get its percepts
     *
     * @param agentId - identifier of the agent
     * @return agent percept
     */
    private Percept getCurrentPercepts(String agentId) {
        CarAgentInfo carInfo = registeredCars.get(agentId);
        double pos = carInfo.getPos();
        Road road = carInfo.getRoad();
        Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
        Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);
        return new CarPercept(pos, nearestCar, nearestSem);
    }

    /**
     *
     * Called by agent to submit an action to the environment
     *
     * @param act - the action
     */
    private void submitAction(Action act) {
        submittedActions.add(act);
        count++;
        if (count == nCars) {
            count = 0;
            getSelf().tell(new Message<>("process-actions", null), ActorRef.noSender());
        }
    }

    /**
     *
     * Called at each simulation step to process the actions
     * submitted by agents.
     *
     */
    private void processActions() {
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
        getContext().actorSelection("/user/gui").tell(new Message<>("new-step", List.of(dt, getContext().system())), ActorRef.noSender());
        System.out.println("Pronto per prox step");
    }

    private Road createRoad(P2d p0, P2d p1) {
        Road r = new Road(p0, p1);
        this.roads.add(r);
        System.out.println("Road created");
        return r;
    }

    private TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
        TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
        this.trafficLights.add(tl);
        return tl;
    }

    // no receive
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

    // no receive
    private Optional<TrafficLightInfo> getNearestSemaphoreInFront(Road road, double carPos, double range){
        return
                road.getTrafficLights()
                        .stream()
                        .filter((TrafficLightInfo tl) -> tl.roadPos() > carPos)
                        .min((c1, c2) -> (int) Math.round(c1.roadPos() - c2.roadPos()));
    }

    private List<CarAgentInfo> getAgentInfo(){
        return this.registeredCars.entrySet().stream().map(el -> el.getValue()).toList();
    }

    private void registerNewCar(String id, CarAgentActor car, Road road, double pos) {
        registeredCars.put(id, new CarAgentInfo(car, road, pos));
    }

    private List<Road> getRoads(){
        return roads;
    }

    private List<TrafficLight> getTrafficLights(){
        return trafficLights;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "get-id".equals(message.name()), message -> getSender().tell(getId(), getSelf()))
                .match(Message.class, message -> "init".equals(message.name()), message -> init((Integer) message.contents().get(0), (Integer) message.contents().get(1)))
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.contents().get(0)))
                .match(Message.class, message -> "get-current-percepts".equals(message.name()), message -> getSender().tell(getCurrentPercepts((String) message.contents().get(0)), getSelf()))
                .match(Message.class, message -> "submit-action".equals(message.name()), message -> submitAction((Action) message.contents().get(0)))
                .match(Message.class, message -> "process-actions".equals(message.name()), message -> processActions())
                .match(Message.class, message -> "create-road".equals(message.name()), message -> getSender().tell(createRoad((P2d) message.contents().get(0), (P2d) message.contents().get(1)), getSelf()))
                .match(Message.class, message -> "create-traffic-light".equals(message.name()), message -> getSender().tell(createTrafficLight((P2d) message.contents().get(0), (TrafficLight.TrafficLightState) message.contents().get(1), (Integer) message.contents().get(2), (Integer) message.contents().get(3), (Integer) message.contents().get(4)), getSelf()))
                .match(Message.class, message -> "get-agent-info".equals(message.name()), message -> getSender().tell(getAgentInfo(), getSelf()))
                .match(Message.class, message -> "get-roads".equals(message.name()), message -> getSender().tell(getRoads(), getSelf()))
                .match(Message.class, message -> "get-traffic-lights".equals(message.name()), message -> getSender().tell(getTrafficLights(), getSelf()))
                .match(Message.class, message -> "register-car".equals(message.name()), message -> registerNewCar((String) message.contents().get(0), (CarAgentActor) message.contents().get(1), (Road) message.contents().get(2), (Double) message.contents().get(3)))
                .build();
    }

}
