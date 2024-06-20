package org.simulation.seq.util;

import org.simulation.seq.environment.Road;
import org.simulation.seq.environment.TrafficLight;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
