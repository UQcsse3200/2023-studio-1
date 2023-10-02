package com.csse3200.game.components.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;

public class CoordinatesDisplay extends UIComponent {
    private Window window;
    private boolean isOpen;
    private final ClueComponent clueComponent;

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
        window = new Window("Ship Part Clue", skin);

        Vector2 clueLocation = clueComponent.getCurrentLocation();
        Label coordinatesLabel = new Label("Search around here...\nX: " + clueLocation.x + "\nY: " + clueLocation.y, skin);
        coordinatesLabel.setColor(Color.BROWN);
        coordinatesLabel.setAlignment(Align.center);

        window.add(coordinatesLabel);

        window.setVisible(false);
        window.pack();
        stage.addActor(window);
    }

    public void toggleOpen() {
        if (isOpen) {
            window.setVisible(false);
            isOpen = false;
        } else {
            window.setVisible(true);
            isOpen = true;
        }
    }

    public void dispose() {
        window.clear();
        window.remove();

        super.dispose();
    }

}
