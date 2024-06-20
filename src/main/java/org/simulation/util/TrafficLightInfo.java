package org.simulation.util;

import org.simulation.environment.Road;
import org.simulation.environment.TrafficLight;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
