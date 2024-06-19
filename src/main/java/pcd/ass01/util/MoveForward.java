package pcd.ass01.util;

import pcd.ass01.agent.Action;

/**
 * Car agent move forward action
 */
public record MoveForward(String agentId, double distance) implements Action {}
