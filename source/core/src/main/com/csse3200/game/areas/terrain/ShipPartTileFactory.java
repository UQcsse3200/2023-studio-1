package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityIndicator;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;

public class ShipPartTileFactory {

	/**
	 * To hide implicit public constructor
	 */
	private ShipPartTileFactory() {
		throw new IllegalArgumentException("Utility class");
	}

	/**
	 * Creates an Entity that contains a ShipPartTileComponent.
	 *
	 * @param position where the entity will be placed.
	 * @return Entity shipPartTile
	 */
	public static Entity createShipPartTile(Vector2 position) {
		DynamicTextureRenderComponent renderComponent = new DynamicTextureRenderComponent("images/ship/ship_part_tile.png");
		renderComponent.setLayer(1);

		Entity shipPartTile = new Entity(EntityType.SHIP_PART_TILE)
				.addComponent(new ColliderComponent().setSensor(true))
				.addComponent(new PhysicsComponent())
				.addComponent(new ShipPartTileComponent())
				.addComponent(renderComponent);

		shipPartTile.addComponent(new EntityIndicator(shipPartTile, "images/ship/part_tile_indicator.png"));

		shipPartTile.setPosition(position);
		return shipPartTile;
	}
}
