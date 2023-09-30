package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the current oxygen level on the Main Game Screen.
 */
public class HealthDisplay extends UIComponent{

    Table table = new Table();
    Group group = new Group();
    private Image healthOutline;
    private Image healthFill;
    private Image healthNormal;
    private Image healthDanger;
    private Array<Label> healthLabels;
    private Label healthLabel;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();

        // Adds a listener to check for health updates
        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        updatePlayerHealthUI(100);
    }

    /**
     * Initialises all the possible images and labels that will be used by
     * the class, and stores them in an array to be called when needed.
     */
    public void createTexture() {
        Skin healthSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        healthOutline = new Image(ServiceLocator.getResourceService().getAsset(
            "images/bars_ui/bar_outline.png", Texture.class));
        healthNormal = new Image(ServiceLocator.getResourceService().getAsset(
            "images/bars_ui/healthy_fill.png", Texture.class));
        healthDanger = new Image(ServiceLocator.getResourceService().getAsset(
            "images/bars_ui/danger_fill.png", Texture.class));

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

        // Accounts for scaling of the oxygen bar due to the oxygen percent
        if (health <= 25) {
            healthFill = healthDanger;
            healthFill.setX(healthFill.getImageX() + 14 * (1 - scaling));
            healthFill.setScaleX(scaling);
        } else {
            healthFill = healthNormal;
            healthFill.setX(healthFill.getImageX() + 14 * (1 - scaling));
            healthFill.setScaleX(scaling);
        }

        // Add a safety check to ensure that the array is always accessed at a possible index
        if (0 <= health && health <= 100) {
            healthLabel = healthLabels.get(health);
            healthLabel.setPosition(healthOutline.getImageX() + 125f, healthOutline.getImageY() + 8.5f);
        }
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
        table.padTop(-130f).padLeft(320f);

        group.addActor(healthOutline);
        group.addActor(healthFill);
        group.addActor(healthLabel);

        table.add(group).size(200);
        stage.addActor(table);
    }

    /**
     * Removes all entities from the screen. Releases all resources from this class.
     */
    @Override
    public void dispose() {
        super.dispose();
        healthOutline.remove();
        healthFill.remove();
        healthLabel.remove();
    }
}
