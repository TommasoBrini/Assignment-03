package org.simulation.actors.environment;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
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
    private int dt;
    protected List<Action> submittedActions;
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
        registeredCars = new HashMap<>();
        trafficLights = new ArrayList<>();
        roads = new ArrayList<>();
        submittedActions = new ArrayList<>();
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
        if (this.actualNumStep == 10) {
            getContext().system().terminate();
            return;
        }
        System.out.println("ENV STEP" + this.actualNumStep + "... max step: " + this.nstep);
        for (var tl: trafficLights) {
            tl.step(dt);
        }
        this.actualNumStep++;
        /* clean actions */
        getSelf().tell(new Message<>("clear-actions", null), ActorRef.noSender());

        /* ask each agent to make a step */
        getContext().actorSelection("/user/car-*").tell(new Message<>("step", dt), ActorRef.noSender());
    }

    private Road createRoad(P2d p0, P2d p1) {
        Road r = new Road(p0, p1);
        this.roads.add(r);
        return r;
    }

    private TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
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
    private void getCurrentPercepts(String agentId) {
        /* TODO: gestire registeredCards
        CarAgentInfo carInfo = registeredCars.get(agentId);
        double pos = carInfo.getPos();
        Road road = carInfo.getRoad();
        Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
        Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);
        */
        getContext().actorSelection("/user/" + agentId).tell(new Message<>("percepts", dt, new CarPercept(1.0, Optional.empty(), Optional.empty())), ActorRef.noSender());
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
        count++;
        if (count == nCars) {
            count = 0;
            getSelf().tell(new Message<>("process-actions", null), ActorRef.noSender());
        }
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
    private void processActions() {
        // TODO: gestire le azioni da fare
        /*for (var act: submittedActions) {
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
        }*/
        System.out.println("Pronto per prox step");

        getSelf().tell(new Message<>("step", dt), ActorRef.noSender());
    }

    private List<CarAgentInfo> getAgentInfo(){
        return this.registeredCars.entrySet().stream().map(el -> el.getValue()).toList();
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
                .match(Message.class, message -> "get-id".equals(message.name()), message -> getSelf().tell(id, getSelf()))
                .match(Message.class, message -> "init".equals(message.name()), message -> init((Integer) message.content(), (Integer) message.content2().get()))
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.content()))
                .match(Message.class, message -> "get-current-percepts".equals(message.name()), message -> getCurrentPercepts((String) message.content()))
                .match(Message.class, message -> "submit-action".equals(message.name()), message -> {
                    // TODO: non so perchè non va bene
                    // this.submitAction((MoveForward) message.content());
                    this.submitAction(new MoveForward("1", 1.0));
                })
                .match(Message.class, message -> "clean-actions".equals(message.name()), message -> cleanActions())
                .match(Message.class, message -> "process-actions".equals(message.name()), message -> processActions())
                .build();
    }

}
