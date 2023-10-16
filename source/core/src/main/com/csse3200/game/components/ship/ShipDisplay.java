package com.csse3200.game.components.ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ShipDisplay extends UIComponent {
	private Window window;
	private boolean isOpen;
	private static final Logger logger = LoggerFactory.getLogger(ShipDisplay.class);

	private boolean lightUnlocked = false;
	private boolean sleepUnlocked = false;
	private int repairsMade = 0;
	private static final String TEXT_COLOUR = "black";
	private static final String PIXEL_BODY = "pixel-body";
	private static final String BACKGROUND_COLOUR = "small-grey";

	@Override
	public void create() {
		super.create();
		isOpen = false;
		addActors();

		entity.getEvents().addListener("interact", this::toggleOpen);
		entity.getEvents().addListener(ShipFactory.events.PROGRESS_UPDATED.name(), this::progressUpdated);
	}

	private void progressUpdated(int repairsMade, Set<ShipProgressComponent.Feature> unlockedFeatures) {
		this.repairsMade = repairsMade;
		if (unlockedFeatures.contains(ShipProgressComponent.Feature.LIGHT)) {
			logger.debug("Ship TimeSkip unlocked");
			lightUnlocked = true;
		}
		if (unlockedFeatures.contains(ShipProgressComponent.Feature.BED)) {
			logger.debug("Ship TimeSkip unlocked");
			sleepUnlocked = true;
		}

	}

	private void addActors() {
		window = new Window("Ship Control", skin);
		window.setVisible(false);
		stage.addActor(window);
	}

	private void updateWindow() {
		window.setMovable(false);
		window.pad(50, 10, 10, 10);
		window.pack();
		window.setWidth(800f);
		window.setPosition(
				stage.getWidth() / 2 - window.getWidth() / 2,
				stage.getHeight() / 2 - window.getHeight() / 2
		);
	}

	private void generateShipControls() {
		window.clear();
		window.getTitleLabel().setText("Ship Controls");
		TextButton toggleLightButton = new TextButton("Ship Light not unlocked", skin, BACKGROUND_COLOUR);

		Label descriptionLabel = new Label("It seems like the ship needs some improvements.\n" + repairsMade + " repairs out of 10.", skin, PIXEL_BODY, TEXT_COLOUR);
		descriptionLabel.setAlignment(Align.center);
		if (lightUnlocked) {

			toggleLightButton = new TextButton("Toggle Light", skin);
			toggleLightButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					entity.getEvents().trigger("toggleLight");
				}
			});
		}
		TextButton timeSkipButton = new TextButton("Time skip not unlocked", skin, BACKGROUND_COLOUR);

		if (sleepUnlocked) {
			timeSkipButton = new TextButton("Sleep (skip the night)", skin);
			timeSkipButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					entity.getEvents().trigger("timeSkip");
				}
			});
		}
		if (sleepUnlocked && lightUnlocked) {
			descriptionLabel = new Label("Congratulations the ship is fully built.", skin, PIXEL_BODY, TEXT_COLOUR);
			descriptionLabel.setAlignment(Align.center);
		}
		TextButton closeButton = new TextButton("Close", skin);
		closeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				toggleOpen();
			}
		});

		Table contentTable = new Table();

		contentTable.defaults().size(600f, 50f).padBottom(10f);
		contentTable.row().pad(30f);

		contentTable.add(descriptionLabel).fillX();
		contentTable.row().pad(30f);
		contentTable.add(toggleLightButton).fillX();
		contentTable.row().pad(10f);
		contentTable.add(timeSkipButton).fillX();
		contentTable.row().pad(10f);
		contentTable.add(closeButton).fillX();

		window.add(contentTable).expand().fill();
		updateWindow();
	}

	@Override
	public void draw(SpriteBatch batch) {
		// Handled else where
	}

	public void toggleOpen() {
		if (isOpen) {
			window.setVisible(false);
			isOpen = false;
		} else {
			generateShipControls();
			window.setVisible(true);
			isOpen = true;
		}
	}

	@Override
	public void dispose() {
		window.clear();
		super.dispose();
	}

	@Override
	public void write(Json json) {
		json.writeObjectStart(this.getClass().getSimpleName());
		json.writeValue("lightUnlocked", this.lightUnlocked);
		json.writeValue("sleepUnlocked", this.sleepUnlocked);
		json.writeValue("repairs", this.repairsMade);
		json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonValue) {
		lightUnlocked = jsonValue.getBoolean("lightUnlocked");
		sleepUnlocked = jsonValue.getBoolean("sleepUnlocked");
		repairsMade = jsonValue.getInt("repairs");
	}

}
