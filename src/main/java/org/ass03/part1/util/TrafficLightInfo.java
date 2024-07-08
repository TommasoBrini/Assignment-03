package org.ass03.part1.util;

import org.ass03.part1.environment.Road;
import org.ass03.part1.environment.TrafficLight;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
