package org.simulation.actors.util;

import java.util.List;

public record Message<T, X>(String name, List<Object> contents){}
