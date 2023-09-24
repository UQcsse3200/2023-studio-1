package com.csse3200.game.components.ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.components.ship.ClueComponent;

public class CoordinatesDisplay extends UIComponent {
    private Label coordinatesLabel;
    private Window window;
    private boolean isOpen;
    private final ClueComponent clueComponent; // Added field

    public CoordinatesDisplay(ClueComponent clueComponent) {
        this.clueComponent = clueComponent;
    }

    public void create() {
        super.create();
        isOpen = false;

        addActors();

        entity.getEvents().addListener("mapDisplay", this::toggleOpen);
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    private void addActors() {
    }

    public void setCoordinates(float x, float y) {
        coordinatesLabel.setText("X: " + x + "\nY: " + y);
    }

    public void toggleOpen() {
        if (isOpen) {
            window.setVisible(false);
            isOpen = false;
        } else {
            String clue = clueComponent.getPossibleLocations().get(0);
            String[] parts = clue.split(",");
            float x = Float.parseFloat(parts[0]);
            float y = Float.parseFloat(parts[1]);
            setCoordinates(x, y);

            window.setVisible(true);
            isOpen = true;
        }
    }

}
