package com.csse3200.game.missions;

public abstract class Mission {

    private final String name;

    public Mission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void registerMission();
    public abstract boolean isCompleted();
    public abstract String getDescription();

}
