package pcd.ass01.environment.implementation;

import java.util.Optional;

import pcd.ass01.environment.Percept;
import pcd.ass01.agent.implementation.CarAgentInfo;

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