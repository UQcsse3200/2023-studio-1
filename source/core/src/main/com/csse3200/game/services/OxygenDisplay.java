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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component for displaying the current oxygen level on the Main Game Screen.
 */
public class OxygenDisplay extends UIComponent {

	private static final Logger logger = LoggerFactory.getLogger(OxygenDisplay.class);
	Table table = new Table();
	Group group = new Group();
	private Image oxygenOutline;
	private Image oxygenFill;
	private Image oxygenHealthy;
	private Image oxygenDanger;
	private Array<Label> oxygenLabels;
	private Label oxygenLabel;

	/**
	 * Creates reusable ui styles and adds actors to the stage.
	 */
	@Override
	public void create() {
		super.create();

		logger.debug("Adding listener to oxygenUpdate event");
		// Adds a listener to check for oxygen updates
		ServiceLocator.getPlanetOxygenService().getEvents()
				.addListener("oxygenUpdate", this::updateDisplay);
		ServiceLocator.getUIService().getEvents()
				.addListener("toggleUI", this::toggleDisplay);

		// Initial update
		updateDisplay();
	}

	private void toggleDisplay(boolean isDisplayed) {
		table.setVisible(isDisplayed);
	}

	/**
	 * Initialises all the possible images and labels that will be used by
	 * the class, and stores them in an array.
	 */
	public void createTexture() {
		logger.debug("Oxygen display texture being created");
		Skin oxygenSkin = ServiceLocator.getResourceService().getAsset("flat-earth/skin/flat-earth-ui.json", Skin.class);

		oxygenOutline = new Image(ServiceLocator.getResourceService().getAsset(
				"images/bars_ui/bar_outline.png", Texture.class));
		oxygenHealthy = new Image(ServiceLocator.getResourceService().getAsset(
				"images/bars_ui/healthy_fill.png", Texture.class));
		oxygenDanger = new Image(ServiceLocator.getResourceService().getAsset(
				"images/bars_ui/danger_fill.png", Texture.class));

		// Set oxygenFill to the initial starting one (VERY IMPORTANT)
		oxygenFill = oxygenDanger;
		oxygenFill.setScaleX(0.1f);

		// Create labels for each percent
		oxygenLabels = new Array<>();
		for (int i = 0; i <= 100; i++) {
//            oxygenLabels.add(new Label(String.format("Oxygen: %d%%", i), oxygenSkin));
			Label label = new Label(String.format("Oxygen: %d%%", i), oxygenSkin);
			Label.LabelStyle labelStyle = label.getStyle();
			labelStyle.fontColor = Color.WHITE; // Set the text color to red (you can choose any color)
			label.setStyle(labelStyle);
			oxygenLabels.add(label);
		}
	}

	/**
	 * Updates the display, showing the oxygen bar in the top of the main game screen.
	 */
	public void updateDisplay() {
		boolean switched;
		if (oxygenLabels == null) {
			createTexture();
		}

		int newOxygenPercent = ServiceLocator.getPlanetOxygenService().getOxygenPercentage();
		float scaling = (float) newOxygenPercent / 100;

		// Adjusts the oxygen bar based on the oxygen percent
		if (newOxygenPercent <= 25) {
			switched = (oxygenFill != oxygenDanger);
			oxygenFill = oxygenDanger;
		} else {
			switched = (oxygenFill != oxygenHealthy);
			oxygenFill = oxygenHealthy;
		}

		oxygenFill.setX(oxygenFill.getImageX() + 14 * (1 - scaling));

		// Ensure that the array is always accessed within bounds
		if (0 <= newOxygenPercent && newOxygenPercent <= 100) {
			logger.debug("Oxygen display updated");
			oxygenLabel = oxygenLabels.get(newOxygenPercent);
			oxygenLabel.setPosition(oxygenOutline.getImageX() + 125f, oxygenOutline.getImageY() + 8.5f);
		}

		if (!switched) {
			oxygenFill.addAction(Actions.scaleTo(scaling, 1.0f, 1.0f, Interpolation.pow2InInverse));
		} else {
			oxygenFill.setScaleX(scaling);
		}
	}

	/**
	 * Draws the table and group onto the main game screen. Adds the oxygen elements onto the stage.
	 *
	 * @param batch Batch to render to.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		table.clear();
		group.clear();
		table.top();
		table.setFillParent(true);

		table.padTop(-130f).padLeft(-180f);


		group.addActor(oxygenOutline);
		group.addActor(oxygenFill);
		group.addActor(oxygenLabel);

		table.add(group).size(200);
		stage.addActor(table);
	}

	/**
	 * Removes all entities from the screen and releases all resources from this class.
	 */
	@Override
	public void dispose() {
		super.dispose();
		oxygenOutline.remove();
		oxygenFill.remove();
		oxygenHealthy.remove();
		oxygenLabel.remove();
	}
}
