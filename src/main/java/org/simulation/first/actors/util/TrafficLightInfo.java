package org.simulation.first.actors.util;

import org.simulation.first.actors.environment.Road;
import org.simulation.first.actors.environment.TrafficLight;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
