package com.csse3200.game.components.items;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class ClueComponentTest {
	private ClueComponent clueComponent;
	private Entity entity;

	@Test
	void testGetCurrentLocation() {
		entity = new Entity();
		clueComponent = new ClueComponent();

		entity.addComponent(this.clueComponent);
		entity.create();

		Vector2 expectedLocation = new Vector2(30, 85);
		Vector2 actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
		clueComponent = new ClueComponent();


		expectedLocation = new Vector2(30, 60);
		actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
		clueComponent = new ClueComponent();



		expectedLocation = new Vector2(30, 40);
		actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
		clueComponent = new ClueComponent();


		expectedLocation = new Vector2(20, 75);
		actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
		clueComponent = new ClueComponent();

		expectedLocation = new Vector2(20, 40);
		actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
		clueComponent = new ClueComponent();



		expectedLocation = new Vector2(30, 75);
		actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
		clueComponent = new ClueComponent();



		expectedLocation = new Vector2(20, 60);
		actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
		clueComponent = new ClueComponent();


		expectedLocation = new Vector2(30, 85);
		actualLocation = clueComponent.getCurrentBaseLocation();

		assertEquals(expectedLocation.x, actualLocation.x);
		assertEquals(expectedLocation.y, actualLocation.y);
	}
}