package com.csse3200.game.components.ship;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClueComponent extends Component {
	/**
	 * A list of all possible base locations. Static so that consequent Map Items don't always
	 * generate a tile in the same spot.
	 */
	private static final List<Vector2> possibleBaseLocations = new ArrayList<>(Arrays.asList(
			new Vector2(30, 85),
			new Vector2(20, 60),
			new Vector2(30, 75),
			new Vector2(20, 40),
			new Vector2(20, 75),
			new Vector2(30, 40),
			new Vector2(30, 60)
	));

	private final Vector2 currentBaseLocation;

	public ClueComponent() {
		currentBaseLocation = possibleBaseLocations.get(0);
		Collections.rotate(possibleBaseLocations, 1);
	}

	public void create() {
		super.create();
		entity.getEvents().addListener("destroy", this::destroy);
	}

	/**
	 * Gets the base location of this item.
	 *
	 * @return Vector2 the base location.
	 */
	public Vector2 getCurrentBaseLocation() {
		return currentBaseLocation;
	}

	/**
	 * Removes the parent entity (item) from the player's inventory.
	 */
	void destroy() {
		ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class).removeItem(entity);
		entity.dispose();
	}
}