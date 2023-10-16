package com.csse3200.game.components.ship;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ShipProgressComponentTest {
	private Entity ship;
	private ShipProgressComponent shipProgressComponent;

	@BeforeEach
	void initialiseTest() {
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerMissionManager(new MissionManager());

		GameArea mockGameArea = mock(GameArea.class);
		when(mockGameArea.getPlayer()).thenReturn(new Entity().addComponent(new InventoryComponent()));
		ServiceLocator.registerGameArea(mockGameArea);

		shipProgressComponent = spy(new ShipProgressComponent());
		ship = new Entity().addComponent(shipProgressComponent);

		ship.create();
	}

	@Test()
	void testIncrement() {
		try (MockedStatic<ItemFactory> itemFactory = mockStatic(ItemFactory.class)){
			itemFactory.when(ItemFactory::createTeleportDevice).thenReturn(new Entity());

			ship.getEvents().trigger(ShipFactory.events.ADD_PART.name(), 1);
			assertEquals(shipProgressComponent.getProgress(), 1);
			assertEquals(shipProgressComponent.getUnlockedFeatures().size(), 0);

			ship.getEvents().trigger(ShipFactory.events.ADD_PART.name(), 1);
			assertEquals(shipProgressComponent.getProgress(), 2);
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.LIGHT));

			ship.getEvents().trigger(ShipFactory.events.ADD_PART.name(), 4);
			assertEquals(shipProgressComponent.getProgress(), 6);
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.LIGHT));
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.BED));

			ship.getEvents().trigger(ShipFactory.events.ADD_PART.name(), 4);
			assertEquals(shipProgressComponent.getProgress(), 10);
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.LIGHT));
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.BED));
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.TELEPORT));
		}
	}

	@Test()
	void testDecrement() {
		try (MockedStatic<ItemFactory> itemFactory = mockStatic(ItemFactory.class)){
			itemFactory.when(ItemFactory::createTeleportDevice).thenReturn(new Entity());
			// Start by unlocking all features
			ship.getEvents().trigger(ShipFactory.events.ADD_PART.name(), 10);
			ship.getEvents().trigger(ShipFactory.events.REMOVE_PART.name(), 4);
			// Ensure the feature remains unlocked
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.TELEPORT));
			ship.getEvents().trigger(ShipFactory.events.REMOVE_PART.name(), 6);
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.BED));
			assertTrue(shipProgressComponent.getUnlockedFeatures().contains(ShipProgressComponent.Feature.LIGHT));
		}
	}
}
