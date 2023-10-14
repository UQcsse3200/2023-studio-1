package com.csse3200.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private Entity entityToTractor;

    /**
     * The camera component of the game
     */
    private CameraComponent cameraComponent;

    /**
     * The space between the edge of the screen and the indicator
     */
    private static final float OFFSET = 8;

    private static final float INDICATOR_DISTANCE = 175; // Distance from center
    private static final float INDICATOR_ROTATION_SPEED = 45; // Rotation speed in degrees per second

    /**
     * Initialise a new indicator for the given entity
     * @param entity: the entity which will be tracked
     */
    public EntityIndicator(Entity entity) {
        this.entityToTractor = entity;
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
        if (cameraComponent.entityOnScreen(entityToTractor)) {
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
        float indicatorPosX;
        float indicatorPosY;

        // Get the latest entity position and transform it to a 3D vector in on-screen units based of the camera view
        Vector2 entityPosition = entityToTractor.getCenterPosition();
        Vector3 entityPos = new Vector3(entityPosition.x, entityPosition.y, 0);
        cameraComponent.getCamera().project(entityPos);

        // Get the center of the screen and adjust the x and y coordinates to correctly position indicator
        Vector3 centerPosition = new Vector3((Gdx.graphics.getWidth() / 2f) + 73, (Gdx.graphics.getHeight() / 2f) + 54, 0);

        // Get the distance vector to the entity, and normalise it, and get the angle to the entity
        Vector3 toEntity = entityPos.cpy().sub(centerPosition);
        toEntity.nor();
        float angle = MathUtils.atan2(toEntity.y, toEntity.x);

        // Calculate the new position for the indicator
        float indicatorX = centerPosition.x + INDICATOR_DISTANCE * toEntity.x;
        float indicatorY = centerPosition.y + INDICATOR_DISTANCE * toEntity.y;

        // Set the position of the indicator
        indicator.setPosition(indicatorX - indicator.getWidth() / 2f, indicatorY - indicator.getHeight() / 2f);

        // Rotate the indicator to point toward the entity
        // -90 is to adjust for initial rotation
        indicator.setRotation(angle * MathUtils.radiansToDegrees - 90);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Handled else where
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