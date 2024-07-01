package org.simulation.part1.util;

/**
 * Car agent move forward action
 */
public record MoveForward(String agentId, double distance) implements Action {}
