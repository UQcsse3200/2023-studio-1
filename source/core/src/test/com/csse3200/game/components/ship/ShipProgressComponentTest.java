package com.csse3200.game.components.ship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
public class ShipProgressComponentTest {
	private Entity ship;
	private ShipProgressComponent shipProgressComponent;

	@BeforeEach
	void initialiseTest() {
		shipProgressComponent = spy(new ShipProgressComponent());
		ship = new Entity().addComponent(shipProgressComponent);

		ship.create();
	}

	@Test()
	void testIncrement() {
		ship.getEvents().trigger("addPart", 1);
		assertEquals(shipProgressComponent.getProgress(), 1);
		assertEquals(shipProgressComponent.getUnlockedFeatures().size(), 0);

		ship.getEvents().trigger("addPart", 2);
		assertEquals(shipProgressComponent.getProgress(), 3);
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.BED));

		ship.getEvents().trigger("addPart", 5);
		assertEquals(shipProgressComponent.getProgress(), 8);
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.BED));
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.LIGHT));

		ship.getEvents().trigger("addPart", 7);
		assertEquals(shipProgressComponent.getProgress(), 15);
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.BED));
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.LIGHT));
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.STORAGE));
	}

	@Test()
	void testDecrement() {
		// Start by unlocking all features
		ship.getEvents().trigger("addPart", 15);
		ship.getEvents().trigger("removePart", 5);
		// Ensure the feature remains unlocked
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.STORAGE));
		ship.getEvents().trigger("removePart", 10);
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.BED));
		assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.LIGHT));
	}
}
