package pcd.ass01.environment.implementation;

import pcd.ass01.environment.implementation.Road;
import pcd.ass01.environment.implementation.TrafficLight;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
