package com.csse3200.game.components.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.ui.UIComponent;

/**
 * Shows a UI containing the coordinates of this entity's ClueComponent.
 */
public class CoordinatesDisplay extends UIComponent {
	private Window window;
	private boolean isOpen;

	public CoordinatesDisplay() {

	}

	@Override
	public void create() {
		super.create();
		isOpen = false;

		addActors();

		entity.getEvents().addListener("mapDisplay", this::toggleOpen);
	}

	@Override
	protected void draw(SpriteBatch batch) {
		// Not needed
	}

	/**
	 * Creates a small window containing a label with the coordinates.
	 */
	private void addActors() {
		window = new Window("Ship Part Clue", skin);

		Label coordinatesLabel = new Label("Looks like there's something\nhidden in the dirt...\nFollow the blue indicator\nto find out!", skin);
		coordinatesLabel.setColor(Color.BROWN);
		coordinatesLabel.setAlignment(Align.center);

		window.add(coordinatesLabel);

		window.setVisible(false);
		window.pack();
		stage.addActor(window);
	}

	/**
	 * Opens and closes the UI.
	 */
	public void toggleOpen() {
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
		window.clear();
		window.remove();
		super.dispose();
	}

	@Override
	public void read(Json json, JsonValue jsonMap) {
		window.clear();
		window.remove();
		addActors();
	}
}
