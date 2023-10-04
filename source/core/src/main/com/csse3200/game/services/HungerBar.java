package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;

public class HungerBar extends UIComponent {
	Table table1 = new Table();
	Group group1 = new Group();
	private Image hungerBarOutline;
	private Image hungerBarFill;
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

		updateDisplay(30);
	}

	/**
	 * Initialises all the possible images and labels that will be used by
	 * the class, and stores them in an array to be called when needed.
	 */
	public void createTexture() {
		Skin hungerSkin = ServiceLocator.getResourceService().getAsset("flat-earth/skin/flat-earth-ui.json", Skin.class);

		hungerBarOutline = new Image(ServiceLocator.getResourceService().getAsset(
				"images/bars_ui/bar_outline.png", Texture.class));
		hungerBarFill = new Image(ServiceLocator.getResourceService().getAsset(
				"images/bars_ui/healthy_fill.png", Texture.class));


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
		hungerBarFill.setX(hungerBarFill.getImageX() + 14 * (1 - scaling));
		hungerBarFill.setScaleX(scaling);

//        hungerBarOutline.setPosition(-550f, -5f);

		// Add a safety check to ensure that the array is always accessed at a possible index
		if (0 <= hungerLevel && hungerLevel <= 100) {
			hungerLabel = hungerLabels.get(hungerLevel);
			hungerLabel.setPosition(hungerBarOutline.getImageX() + 125f, hungerBarOutline.getImageY() + 8.5f);
		}

	}

	/**
	 * Draws the table and group onto the main game screen. Adds the hunger elements onto the stage.
	 *
	 * @param batch Batch to render to.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		table1.clear();
		group1.clear();
		table1.top();
		table1.setFillParent(true);

		table1.padTop(-130f).padLeft(-1000f);


		group1.addActor(hungerBarOutline);
		group1.addActor(hungerBarFill);
		group1.addActor(hungerLabel);

		table1.add(group1).size(200);
		stage.addActor(table1);
	}

	/**
	 * Removes all entities from the screen. Releases all resources from this class.
	 */
	@Override
	public void dispose() {
		super.dispose();
		hungerBarOutline.remove();
		hungerBarFill.remove();
		hungerLabel.remove();
	}
}
