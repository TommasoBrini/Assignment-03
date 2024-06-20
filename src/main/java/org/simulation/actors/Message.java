package org.simulation.actors;

public record Message<T>(String name, T content) { }
