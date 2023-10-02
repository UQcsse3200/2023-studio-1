package com.csse3200.game.components.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;

/**
 * Shows a UI containing the coordinates of this entity's ClueComponent.
 */
public class CoordinatesDisplay extends UIComponent {
	private Window window;
	private boolean isOpen;
	private final ClueComponent clueComponent;

	public CoordinatesDisplay(ClueComponent clueComponent) {
		this.clueComponent = clueComponent;
	}

	public void create() {
		super.create();
		isOpen = false;

		addActors();

		entity.getEvents().addListener("mapDisplay", this::toggleOpen);
	}

	@Override
	protected void draw(SpriteBatch batch) {

	}

	/**
	 * Creates a small window containing a label with the coordinates.
	 */
	private void addActors() {
		window = new Window("Ship Part Clue", skin);

		Vector2 clueLocation = clueComponent.getCurrentBaseLocation();
		Label coordinatesLabel = new Label("Search around here...\nX: " + clueLocation.x + "\nY: " + clueLocation.y, skin);
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

	public void dispose() {
		window.clear();
		window.remove();

		super.dispose();
	}

}
