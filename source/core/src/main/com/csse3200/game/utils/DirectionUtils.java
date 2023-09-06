package com.csse3200.game.utils;

public class DirectionUtils {
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    public static String angleToDirection(float angle) {
        angle %= 360f;

        if (angle < 45 || angle >= 315) {
            return RIGHT;
        } else if (angle < 135) {
            return UP;
        } else if (angle < 225) {
            return LEFT;
        } else {
            return DOWN;
        }
    }
}
