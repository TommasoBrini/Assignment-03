package org.simulation.part1.util;

import org.simulation.part1.environment.Road;
import org.simulation.part1.environment.TrafficLight;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
