package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HungerBar extends UIComponent{
    private static final Logger logger = LoggerFactory.getLogger(HungerBar.class);
    Table table = new Table();
    Group group = new Group();
    private Image hungerFrame;
    private Image hungerFill;
    private Image hungerIcon;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();

        createTexture();

        logger.debug("Adding listener to hungerUpdate event");
        // Adds a listener to check for hunger updates
        ServiceLocator.getPlayerHungerService().getEvents()
                .addListener("hungerUpdate", this::updateDisplay);
        ServiceLocator.getUIService().getEvents()
                .addListener("toggleUI", this::toggleDisplay);

        updateDisplay(30);
    }

    private void toggleDisplay(boolean isDisplayed) {
        table.setVisible(isDisplayed);
    }

	/**
	 * Initialises all the possible images and labels that will be used by
	 * the class, and stores them in an array to be called when needed.
	 */
	public void createTexture() {

        logger.debug("Hunger display texture being created");

        hungerFrame = new Image(ServiceLocator.getResourceService().getAsset(
                "images/status_ui/status_frame.png", Texture.class));
        hungerFill = new Image(ServiceLocator.getResourceService().getAsset(
                "images/status_ui/hunger_fill.png", Texture.class));
        hungerIcon = new Image(ServiceLocator.getResourceService().getAsset(
                "images/status_ui/hunger_icon.png", Texture.class));

        hungerFill.setScaleY(1.0f);
    }

    /**
     * Updates the display, showing the hunger bar in the top of the main game screen.
     */
    public void updateDisplay(int hungerLevel) {

        logger.debug("Hunger display updated");

        float scaling = (float) hungerLevel / 100;
        float targetY = hungerFill.getImageY() + 48 * (1 - scaling);

        // Moves the hunger filling to the correct position, while it is being scaled to give a nice transition.
        Action action1 = Actions.moveTo(hungerFill.getX(), targetY, 1.0f, Interpolation.pow2InInverse);
        Action action2 = Actions.scaleTo(1.0f, scaling, 1.0f, Interpolation.pow2InInverse);

        hungerFill.addAction(Actions.parallel(action1, action2));
    }

    /**
     * Draws the table and group onto the main game screen. Adds the hunger elements onto the stage.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        table.clear();
        group.clear();
        table.top();
        table.setFillParent(true);

        table.padTop(-50f).padLeft(-50f);

        group.addActor(hungerFill);
        group.addActor(hungerFrame);
        group.addActor(hungerIcon);

        table.add(group).size(200);
        stage.addActor(table);
    }

    /**
     * Removes all entities from the screen. Releases all resources from this class.
     */
    @Override
    public void dispose() {
        super.dispose();
        hungerFrame.remove();
        hungerFill.remove();
        hungerIcon.remove();
    }
}
