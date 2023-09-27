package com.csse3200.game.components.ship;

import java.util.ArrayList;
import java.util.List;

import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;
public class ClueComponent extends Component {

    private List<String> possibleLocations;
    private MissionManager missionManager;

    public ClueComponent(List<String> possibleLocations) {
        this.possibleLocations = possibleLocations;
    }

    public void create(List<String> possibleLocations) {
        super.create();
        missionManager = ServiceLocator.getMissionManager();
        this.possibleLocations = possibleLocations;
        entity.getEvents().addListener("destroy", this::destroy);
    }

    public List<String> getPossibleLocations() {
        return possibleLocations;
    }

    void destroy(TerrainTile tile) {
        entity.dispose();
    }
}