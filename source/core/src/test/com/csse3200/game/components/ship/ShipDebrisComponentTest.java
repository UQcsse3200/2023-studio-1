package com.csse3200.game.components.ship;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import java.util.Random;

import static com.csse3200.game.missions.quests.QuestFactory.ALIENS_ATTACK_QUEST_NAME;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ShipDebrisComponentTest {
	void setupDummyServices() {
		ServiceLocator.registerRenderService(new RenderService());
		ResourceService resourceService = new ResourceService();
		resourceService.loadTextureAtlases(new String[]{"images/shipeater.atlas", "images/animals/animal_effects.atlas"});
		resourceService.loadTextures(new String[]{"images/hostile_indicator.png"});
		resourceService.loadAll();
		ServiceLocator.registerResourceService(resourceService);

		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerGameArea(new SpaceGameArea(null));

		ServiceLocator.registerPhysicsService(new PhysicsService());

		ServiceLocator.registerRenderService(new RenderService());

		Stage stage = mock(Stage.class);
		ServiceLocator.getRenderService().setStage(stage);
	}
	@Test
	void spawnsShipEaterOnlyAfterRangedWeaponsUnlocked() {
		setupDummyServices();

		MissionManager mockMissionManager = new MissionManager();
		ServiceLocator.registerMissionManager(mockMissionManager);
		ParticleService particleService = mock(ParticleService.class);
		ServiceLocator.registerParticleService(particleService);

		EntityService mockEntityService = spy(EntityService.class);
		ServiceLocator.registerEntityService(mockEntityService);

		ArgumentCaptor<Entity> entityCaptor = ArgumentCaptor.forClass(Entity.class);

		ShipDebrisComponent shipDebrisComponent = new ShipDebrisComponent();
		shipDebrisComponent.setRandomInstance(new Random(12345));
		Entity shipDebris = new Entity(EntityType.SHIP_DEBRIS)
				.addComponent(shipDebrisComponent);
		mockEntityService.register(shipDebris);

		// don't want to count registering the shipDebris in the mock
		clearInvocations(mockEntityService);

		shipDebris.getEvents().trigger("destroy", null);

		// quest that unlocks guns not complete yet, shouldn't spawn

		verify(mockEntityService, times(0)).register(any(Entity.class));

		// unlock the quest

		mockMissionManager.getEvents().trigger(MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), ALIENS_ATTACK_QUEST_NAME);

		shipDebrisComponent = new ShipDebrisComponent();
		shipDebrisComponent.setRandomInstance(new Random(12345));
		shipDebris = new Entity(EntityType.SHIP_DEBRIS)
				.addComponent(shipDebrisComponent);
		mockEntityService.register(shipDebris);

		// don't want to count registering the shipDebris in the mock
		clearInvocations(mockEntityService);

		shipDebris.getEvents().trigger("destroy", null);

		// should spawn now

		verify(mockEntityService, times(1)).register(entityCaptor.capture());
		Entity entity = entityCaptor.getValue();

		assert(entity.getType() == EntityType.SHIP_EATER);
	}

}
