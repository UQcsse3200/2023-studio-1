package com.csse3200.game.components.missioninformation;

import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.screens.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MissionDisplay extends UIComponent {

    private Window window;

    private boolean isOpen;

    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);

    @Override
    public void create() {
        super.create();
        addActors();
        isOpen = false;
        entity.getEvents().addListener("toggleMissions",this::toggleOpen);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {

        // Create a window for the inventory using the skin
        window = new Window("Missions", skin);
        window.pad(40, 10, 10, 10); // Add padding to with so that the text doesn't go offscreen
        window.pack(); // Pack the window to the size
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
        window.setVisible(false);
        stage.addActor(window);
    }

    /**
     * Draw stage of inventory
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch)  {
    }

    public void toggleOpen(){
        if (isOpen) {
            window.setVisible(false);
            isOpen = false;
        } else {
            window.setVisible(true);
            isOpen = true;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}


