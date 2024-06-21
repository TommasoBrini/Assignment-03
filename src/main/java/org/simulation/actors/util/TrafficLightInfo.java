package org.simulation.actors.util;

import org.simulation.actors.environment.Road;
import org.simulation.actors.environment.TrafficLight;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
