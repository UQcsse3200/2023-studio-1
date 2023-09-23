package com.csse3200.game.components;

import java.util.ArrayList;
import java.util.List;

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
