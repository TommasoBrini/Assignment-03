package org.simulation.actors;

import java.util.List;

public record Message<T, X>(String name, List<Object> contents){}
