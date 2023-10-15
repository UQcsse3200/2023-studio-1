package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.weather.ClimateController;

public class TestGameArea extends GameArea {
    private GameMap gameMap;
    private ClimateController climateController = new ClimateController();

    @Override
    public void create() {
    }

    @Override
    public ClimateController getClimateController() {
        return climateController;
    }

    @Override
    public GameMap getMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
}
