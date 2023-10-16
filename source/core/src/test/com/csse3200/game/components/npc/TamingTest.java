package com.csse3200.game.components.npc;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.*;
import com.csse3200.game.services.sound.EffectsMusicService;
import com.csse3200.game.services.sound.SoundService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class TamingTest {

	private Entity player;
	private Entity animalToTest;
	private InventoryComponent playerInventory;
	private InventoryComponent playerInvSpy;
	private Entity foodEntity;
	private Entity nonFood;
	String[] texturePaths = {"images/tool_shovel.png"};
	SoundService mockSoundService;

	@BeforeEach
	void beforeEach() {
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.getResourceService().loadTextures(texturePaths);
		ServiceLocator.getResourceService().loadAll();
		ServiceLocator.registerTimeSource(new GameTime());
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerMissionManager(new MissionManager());

		mockSoundService = mock(SoundService.class);
		ServiceLocator.registerSoundService(mockSoundService);

		when(mockSoundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));

		playerInventory = new InventoryComponent(new ArrayList<>());
		playerInvSpy = spy(playerInventory);
		player = new Entity().addComponent(playerInvSpy);
		player.create();

		for (int index = 0; index < 4; index++) {
			foodEntity = new Entity(EntityType.ITEM);
			ItemComponent fooditem = new ItemComponent("AFood", ItemType.ANIMAL_FOOD,
					"images/tool_shovel.png"); //texture is just used as a placeholder.
			foodEntity.addComponent(fooditem);
			foodEntity.addComponent(fooditem);
			foodEntity.addComponent(fooditem);
			playerInvSpy.addItem(foodEntity);
		}
	}

	@AfterEach
	void clear() {
		ServiceLocator.clear();
	}

	@Test
	void testUnfed() {
		TamableComponent taming = new TamableComponent(player, 1, 0,
				"Animal Food");
		assertFalse(taming.isTamed());
	}

	@Test
	void testLowTameProbability() {
		playerInvSpy.setHeldItem(0);
		TamableComponent toTame = new TamableComponent(player, 1, 0,
				"AFood");
		animalToTest = new Entity().addComponent(toTame);
		animalToTest.create();

		animalToTest.getEvents().trigger("feed");

		assertTrue(animalToTest.getComponent(TamableComponent.class).isTamed());
	}

	@Test
	void testTameThreshold() {
		TamableComponent toTame = new TamableComponent(player, 2, 1.1,
				"AFood");
		animalToTest = new Entity().addComponent(toTame);
		animalToTest.create();

		for (int loop = 0; loop < 3; loop++) {
			playerInvSpy.setHeldItem(0);
			animalToTest.getEvents().trigger("feed");
		}
		assertTrue(animalToTest.getComponent(TamableComponent.class).isTamed());
	}

	@Test
	void testNotFavouriteFood() {
		TamableComponent toTame = new TamableComponent(player, 2, 1.1,
				"BFood");
		animalToTest = new Entity().addComponent(toTame);
		animalToTest.create();

		playerInvSpy.setHeldItem(0);
		animalToTest.getEvents().trigger("feed");

		assertFalse(animalToTest.getComponent(TamableComponent.class).isTamed());
	}

	@Test
	void feedAnimalNonEdibleEntity() {
		TamableComponent toTame = new TamableComponent(player, 2, 1.1,
				"BFood");
		animalToTest = new Entity().addComponent(toTame);
		animalToTest.create();
		nonFood = new Entity();
		playerInvSpy.addItem(nonFood);
		int index = playerInvSpy.getItemPlace().size();
		playerInvSpy.setHeldItem(index - 1);

		animalToTest.getEvents().trigger("feed");

		assertFalse(animalToTest.getComponent(TamableComponent.class).isTamed());
	}

	@Test
	void testAstrolotlOxygen() {
		playerInvSpy.setHeldItem(0);

		PlanetOxygenService planetOxygenService = new PlanetOxygenService();
		ServiceLocator.registerPlanetOxygenService(planetOxygenService);

		float initialOxygen = planetOxygenService.getOxygen();

		TamableComponent toTame = new TamableComponent(player, 2, 1.1,
				"AFood");

		animalToTest = new Entity(EntityType.ASTROLOTL).addComponent(toTame);
		animalToTest.create();

		animalToTest.getComponent(TamableComponent.class).setTame(true);

		animalToTest.getEvents().trigger("feed");

		float finalOxygen = planetOxygenService.getOxygen();

		assertEquals(75, finalOxygen - initialOxygen);
	}
}
