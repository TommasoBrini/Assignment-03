package org.simulation.actors.car;

import org.simulation.seq.util.Action;
import org.simulation.seq.util.CarPercept;

import java.util.Optional;

public abstract class CarAgentActor extends AbstractAgentActor{
    /* car model */
    protected double maxSpeed;
    protected double currentSpeed;
    protected double acceleration;
    protected double deceleration;

    /* percept and action retrieved and submitted at each step */
    protected CarPercept currentPercept;
    protected Optional<Action> selectedAction;

    public CarAgentActor(String id, double acc, double dec, double vmax) {
        super(id);
        this.acceleration = acc;
        this.deceleration = dec;
        this.maxSpeed = vmax;
        // TODO: register the car in the environment
    }

    protected void step(int dt) {
        System.out.println("Step " + dt + " for car " + getId() + "...");
        /* sense */
        /*
        TODO: ask to env the percepts
         */
        //currentPercept = (CarPercept) env.getCurrentPercepts(getId());

        /* decide */
        selectedAction = Optional.empty();

        decide(dt);

        /* act */
        if (selectedAction.isPresent()) {
            // TODO: submit the action to the environment
            //env.submitAction(selectedAction.get());
        }
    }

    protected abstract void decide(int dt);

    protected double getCurrentSpeed() {
        return currentSpeed;
    }

}
