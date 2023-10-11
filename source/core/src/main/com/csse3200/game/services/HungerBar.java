package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Color;
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
import com.csse3200.game.ui.UIComponent;

public class HungerBar extends UIComponent{
    Table table = new Table();
    Group group = new Group();
    private Image hungerFrame;
    private Image hungerFill;
    private Image hungerIcon;
    private Array<Label> hungerLabels;
    private Label hungerLabel;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();

        // Adds a listener to check for hunger updates
        ServiceLocator.getPlayerHungerService().getEvents()
                .addListener("hungerUpdate", this::updateDisplay);
        ServiceLocator.getUIService().getEvents()
                .addListener("toggleUI", this::toggleDisplay);

        updateDisplay(100);
    }

    private void toggleDisplay(boolean isDisplayed) {
        table.setVisible(isDisplayed);
    }

	/**
	 * Initialises all the possible images and labels that will be used by
	 * the class, and stores them in an array to be called when needed.
	 */
	public void createTexture() {
		Skin hungerSkin = ServiceLocator.getResourceService().getAsset("flat-earth/skin/flat-earth-ui.json", Skin.class);

        hungerFrame = new Image(ServiceLocator.getResourceService().getAsset(
                "images/status_ui/status_frame.png", Texture.class));
        hungerFill = new Image(ServiceLocator.getResourceService().getAsset(
                "images/status_ui/hunger_fill.png", Texture.class));
        hungerIcon = new Image(ServiceLocator.getResourceService().getAsset(
                "images/status_ui/hunger_icon.png", Texture.class));


        hungerFill.setScaleY(1.0f);

        hungerLabels = new Array<>();
        for (int i = 0; i <= 100; i++) {
                Label label = new Label(String.format("Hunger: %d%%", i), hungerSkin);
                Label.LabelStyle labelStyle = label.getStyle();
                labelStyle.fontColor = Color.WHITE; // Set the text color to red (you can choose any color)
                label.setStyle(labelStyle);
                hungerLabels.add(label);
        }
    }

    /**
     * Updates the display, showing the hunger bar in the top of the main game screen.
     */
    public void updateDisplay(int hungerLevel) {
        if (hungerLabels == null) {
            createTexture();
        }

		float scaling = (float) hungerLevel / 100;

        // Accounts for scaling of the hunger bar due to the hunger percent
        hungerFill.setY(hungerFill.getImageY() + 48 * (1 - scaling));

        if (0 <= hungerLevel && hungerLevel <= 100) {
            hungerLabel = hungerLabels.get(hungerLevel);
            hungerLabel.setPosition(hungerFrame.getImageX() + 125f, hungerFrame.getImageY() + 8.5f);
        }

        hungerFill.addAction(Actions.scaleTo(1.0f, scaling, 1.0f, Interpolation.fade));

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
        //group.addActor(hungerLabel);
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
        hungerLabel.remove();
    }
}
