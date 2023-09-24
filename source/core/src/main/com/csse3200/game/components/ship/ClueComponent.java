package com.csse3200.game.components.ship;

import java.util.ArrayList;
import java.util.List;
import com.csse3200.game.components.Component;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;

public class ClueComponent extends Component {

    private List<String> possibleLocations;

    public ClueComponent(List<String> possibleLocations) {
        this.possibleLocations = possibleLocations;
    }

    public List<String> getPossibleLocations() {
        return possibleLocations;
    }

    public void setPossibleLocations(List<String> possibleLocations) {
        this.possibleLocations = possibleLocations;
    }
}
