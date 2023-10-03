package com.csse3200.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.components.CameraComponent;


/**
 * A UI component responsible for indicating the direction of an entity which is off-screen
 */
public class EntityIndicator extends UIComponent{

    /**
     * Indicator image
     */
    private Image indicator;

    /**
     * The hostile entity being tracked
     */
    private Entity entity;

    /**
     * X and Y position of the indicator on the game screen
     */
    private float indicatorPosX;
    private float indicatorPosY;

    /**
     * The camera component of the game
     */
    private CameraComponent cameraComponent;

    /**
     * The space between the edge of the screen and the indicator
     */
    private static final float OFFSET = 8;

    /**
     * Initialise a new indicator for the given entity
     * @param entity: the entity which will be tracked
     */
    public EntityIndicator(Entity entity) {
        this.entity = entity;
        cameraComponent = ServiceLocator.getCameraComponent();
    }

    /**
     * Creates the UI and stages the actors
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Stages the UI elements to the display screen
     */
    private void addActors() {
        // NOTE: If you want to use different images for indicators based on the entity type then,
        // use an if-else statement to initialise the indicator
        indicator = new Image(ServiceLocator.getResourceService().getAsset("images/hostile_indicator.png", Texture.class));
        stage.addActor(indicator);
    }

    /**
     * Updates the indicator postion on the UI
     */
    @Override
    public void update() {
        // If the entity is on the screen then there is no reason to display the indicator
        if (cameraComponent.entityOnScreen(entity)) {
            indicator.setVisible(false);
        } else {
            // NOTE: Add more conditions to the if-else statement if you want the indicator to only display at
            // certain times. For example, when an enemy entity attacks your crops. Ideally you can create a
            // function to check that in the entity class then call it here given the entity will already be stored
            indicator.setVisible(true);
            updateIndicator();
        }
    }

    /**
     * Calculates where the entity should be positioned along the edge of the screen
     */
    public void updateIndicator() {
        // Get the latest entity position
        Vector2 entityPosition = entity.getCenterPosition();

        // Make the entity position into a 3D vector
        Vector3 entityPos = new Vector3(entityPosition.x, entityPosition.y, 0);

        // Transforms world coordinates of the entity into screen coordinates based on the cameras view
        cameraComponent.getCamera().project(entityPos);

        // Ensures that the position (entityPos.X - OFFSET) is within the specified range
        // If the position is less than OFFSET then it sets the indicator to OFFSET
        // If its greater than "Gdx.graphics.getWidth() - indicator.getWidth() - OFFSET" then it is set to
        // "Gdx.graphics.getWidth() - indicator.getWidth() - OFFSET"
        indicatorPosX = MathUtils.clamp(entityPos.x - OFFSET, OFFSET,
                Gdx.graphics.getWidth() - indicator.getWidth() - OFFSET);

        indicatorPosY = MathUtils.clamp(entityPos.y - OFFSET, OFFSET,
                Gdx.graphics.getHeight() - indicator.getHeight() - OFFSET);

        // Updates the position of the indicator based on the new coordinates
        indicator.setPosition(indicatorPosX, indicatorPosY);
    }

    @Override
    public void draw(SpriteBatch batch) {
    }

    /**
     * Disposes the indicator from the screen
     */
    @Override
    public void dispose() {
        indicator.clear();
        super.dispose();
    }
}