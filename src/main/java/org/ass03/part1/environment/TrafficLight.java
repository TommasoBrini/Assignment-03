package org.ass03.part1.environment;

import org.ass03.part1.util.P2d;

/**
 * Class modeling the structure and behaviour of a traffic light
 *  
 */
public class TrafficLight {
	
	public enum TrafficLightState {GREEN, YELLOW, RED}
	private TrafficLightState state;
	private final TrafficLightState initialState;
	private int currentTimeInState;
	private final int redDuration;
	private final int greenDuration;
	private final int yellowDuration;
	private final P2d pos;
	
	public TrafficLight(P2d pos, TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		this.redDuration = redDuration;
		this.greenDuration = greenDuration;
		this.yellowDuration = yellowDuration;
		this.pos = pos;
		this.initialState = initialState;
	}
	
	public void init() {
		state = initialState;
		currentTimeInState = 0;
	}

	public void step(int dt) {
		switch (state) {
			case GREEN -> {
				currentTimeInState += dt;
				if (currentTimeInState >= greenDuration) {
					state = TrafficLightState.YELLOW;
					currentTimeInState = 0;
				}
			}
			case RED -> {
				currentTimeInState += dt;
				if (currentTimeInState >= redDuration) {
					state = TrafficLightState.GREEN;
					currentTimeInState = 0;
				}
			}
			case YELLOW -> {
				currentTimeInState += dt;
				if (currentTimeInState >= yellowDuration) {
					state = TrafficLightState.RED;
					currentTimeInState = 0;
				}
			}
			default -> {
			}
		}
	}
	
	public boolean isGreen() {
		return state.equals(TrafficLightState.GREEN);
	}
	
	public boolean isRed() {
		return state.equals(TrafficLightState.RED);
	}

	public boolean isYellow() {
		return state.equals(TrafficLightState.YELLOW);
	}
	
	public P2d getPos() {
		return pos;
	}
}
