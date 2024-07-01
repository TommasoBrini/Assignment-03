package org.simulation.first.actors.util;

import java.util.List;

public record Message(String name, List<Object> contents){}
