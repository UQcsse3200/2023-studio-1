package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component for displaying the current health level on the Main Game Screen.
 */
public class HealthDisplay extends UIComponent{

    private static final Logger logger = LoggerFactory.getLogger(HealthDisplay.class);
    Table table = new Table();
    Group group = new Group();
    private Image healthFrame;
    private Image healthFill;
    private Image healthIcon;
    private Array<Label> healthLabels;
    private Label healthLabel;
    private int currentHealth;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();

        logger.debug("Adding listener to healthUpdate event");
        // Adds a listener to check for health updates
        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        ServiceLocator.getGameArea().getPlayer().getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        ServiceLocator.getUIService().getEvents().addListener("toggleUI", this::toggleDisplay);
        currentHealth = ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class).getHealth();
        updatePlayerHealthUI(currentHealth);

        // Initial update
        updatePlayerHealthUI(currentHealth);
    }

    private void toggleDisplay(boolean isDisplayed) {table.setVisible(isDisplayed);
    }

    /**
     * Initialises all the possible images and labels that will be used by
     * the class, and stores them in an array to be called when needed.
     */
    public void createTexture() {
        logger.debug("Health display texture being created");
        Skin healthSkin = ServiceLocator.getResourceService().getAsset("flat-earth/skin/flat-earth-ui.json", Skin.class);

        healthFrame = new Image(ServiceLocator.getResourceService().getAsset(
            "images/status_ui/status_frame.png", Texture.class));
        healthFill = new Image(ServiceLocator.getResourceService().getAsset(
            "images/status_ui/health_fill.png", Texture.class));
        healthIcon = new Image(ServiceLocator.getResourceService().getAsset(
            "images/status_ui/health_icon.png", Texture.class));

        // Set health fill to the initial starting one
        healthFill.setScaleY(1.0f);

        // Create labels for each percent
        healthLabels = new Array<>();
        for (int i = 0; i <= 100; i++) {
            healthLabels.add(new Label(String.format("Health: %d%%", i), healthSkin));
        }
    }

    /**
     * Updates the display, showing the health bar in the top of the main game screen.
     */
    public void updatePlayerHealthUI(int health) {
        if (healthLabels == null) {
            createTexture();
        }

        float scaling = (float) health / 100;

        // Adjusts the health bar based on the health percent
        healthFill.setY(healthFill.getImageY() + 48 * (1 - scaling));

        // Ensure that the array is always accessed within bounds
        if (0 <= health && health <= 100) {
            logger.debug("Health display updated");
            healthLabel = healthLabels.get(health);
            healthLabel.setPosition(healthFrame.getImageX() + 125f, healthFrame.getImageY() + 8.5f);
        }

        healthFill.addAction(Actions.scaleTo(1.0f, scaling, 1.0f, Interpolation.pow2InInverse));
    }

    /**
     * Draws the table and group onto the main game screen. Adds the health elements onto the stage.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        table.clear();
        group.clear();
        table.top();
        table.setFillParent(true);
        table.padTop(-50f).padLeft(270f);

        group.addActor(healthFill);
        group.addActor(healthFrame);
        //group.addActor(healthLabel);
        group.addActor(healthIcon);

        table.add(group).size(200);
        stage.addActor(table);
    }

    /**
     * Removes all entities from the screen. Releases all resources from this class.
     */
    @Override
    public void dispose() {
        super.dispose();
        healthFrame.remove();
        healthFill.remove();
        healthLabel.remove();
    }
}
