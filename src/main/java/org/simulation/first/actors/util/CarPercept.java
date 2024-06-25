package org.simulation.first.actors.util;

import org.simulation.first.actors.car.CarAgentInfo;

import java.util.Optional;

/**
 * 
 * Percept for Car Agents
 * 
 * - position on the road
 * - nearest car, if present (distance)
 * - nearest semaphore, if present (distance)
 * 
 */
public record CarPercept(double roadPos, Optional<CarAgentInfo> nearestCarInFront, Optional<TrafficLightInfo> nearestSem) implements Percept { }