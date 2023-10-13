package com.csse3200.game.components.combat.attackpatterns;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.plants.PlantCommandService;
import com.csse3200.game.services.plants.PlantInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class AttackPatternTest {

    private AttackPatternComponent attackPatternComponent;
    private Entity entity;
    private InteractionDetector interactionDetector;
    private GameTime gameTime;




    @BeforeEach
    void setUp() {
        // Set up game time
        gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerGameArea(mock(GameArea.class));
        ServiceLocator.registerResourceService(mock(ResourceService.class));

        // Plant stuff
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerPlantCommandService(new PlantCommandService());
        ServiceLocator.registerPlantInfoService(mock(PlantInfoService.class));



        interactionDetector = spy(new InteractionDetector(1.0f));
        entity = new Entity()
                .addComponent(interactionDetector)
                .addComponent(new PhysicsComponent());

        when(interactionDetector.getEntitiesInRange()).thenReturn(List.of(new Entity()));
    }

    @Test
    void testAttackLoopRuns() {
        attackPatternComponent = spy(getAttackPatternComponent(null));
        entity.addComponent(attackPatternComponent);
        entity.create();

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", mock(Entity.class));

        // check runs attack
        verify(attackPatternComponent, times(1)).attack();

        // trigger more 9 attacks
        for (int i = 0; i < 9; i++) {
            when(gameTime.getTime()).thenReturn(1000L + 1000L * i);
            entity.update();
        }

        // check runs attack 10 times
        verify(attackPatternComponent, times(10)).attack();
    }

    @Test
    void testOxygenEaterAttack() {
        attackPatternComponent = spy(getAttackPatternComponent(EntityType.OXYGEN_EATER));

        entity.addComponent(attackPatternComponent);
        entity.create();

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", mock(Entity.class));
        entity.update();

        // check attack starts before shoot
        verify(attackPatternComponent, times(1)).attack();

        // check shoot by checking if entities are added to GameArea
        verify(ServiceLocator.getGameArea(), times(0)).spawnEntity(any());

        // wait for delay
        when(gameTime.getTime()).thenReturn(200L);
        entity.update();

        // verify projectile spawns
        verify(ServiceLocator.getGameArea(), times(1)).spawnEntity(any());

        // pass 9 seconds, with entity exiting range after 3 seconds
        for (int i = 0; i < 9; i++) {
            when(gameTime.getTime()).thenReturn(1000L + 1000L * i);
            if (i == 3) {
                when(interactionDetector.getEntitiesInRange()).thenReturn(new ArrayList<>());
            }
            entity.update();
        }

        // check only 3 more projectiles spawn
        verify(ServiceLocator.getGameArea(), times(4)).spawnEntity(any());
    }

    @Test
    void testDragonflyAttackPlayer() {
        attackPatternComponent = spy(getAttackPatternComponent(EntityType.DRAGONFLY));

        entity.addComponent(attackPatternComponent);
        entity.create();

        Entity playerTarget = new Entity(EntityType.PLAYER);
        Entity plantTarget = new Entity(EntityType.PLANT);

        // Place it close enough to the plant
        entity.setPosition(1f, 1f);
        plantTarget.setPosition(1f, 1f);

        when(interactionDetector.getEntitiesInRange()).thenReturn(List.of(plantTarget, playerTarget));

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", mock(Entity.class));
        entity.update();

        // check attack starts before shoot
        verify(attackPatternComponent, times(1)).attack();

        // check shoot by checking if entities are added to GameArea
        verify(ServiceLocator.getGameArea(), times(0)).spawnEntity(any());

        // wait for delay
        when(gameTime.getTime()).thenReturn(500L);
        entity.update();

        // verify 3 projectiles spawn
        verify(ServiceLocator.getGameArea(), times(3)).spawnEntity(any());

        // pass 9 seconds, with entity exiting range after 3 seconds
        for (int i = 0; i < 9; i++) {
            when(gameTime.getTime()).thenReturn(1000L + 1000L * i);
            if (i == 3) {
                when(interactionDetector.getEntitiesInRange()).thenReturn(new ArrayList<>());
            }
            entity.update();
        }

        // check only 9 more projectiles spawn
        verify(ServiceLocator.getGameArea(), times(12)).spawnEntity(any());

    }

    @Test
    void testDragonflyAttackPlant() {
        attackPatternComponent = spy(getAttackPatternComponent(EntityType.DRAGONFLY));

        entity.addComponent(attackPatternComponent);
        entity.create();

        Entity plantTarget = new Entity(EntityType.PLANT);

        int[] growthStageThresholds = new int[]{1,2,3};
        String[] soundArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        CropTileComponent mockCropTile = mock(CropTileComponent.class);

        PlantComponent plantComponent = new PlantComponent(500, "testPlant", "DEFENCE", "test " +
                "plant", 1, 2, 1000, mockCropTile, growthStageThresholds, soundArray);

        plantTarget.addComponent(plantComponent);
        plantTarget.create();

        int plantStartingHealth = plantComponent.getPlantHealth();

        // Place it close enough to the plant
        entity.setPosition(1f, 1f);
        plantTarget.setPosition(1f, 1f);

        when(interactionDetector.getEntitiesInRange()).thenReturn(List.of(plantTarget));

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", plantTarget);
        entity.update();

        assertTrue(plantStartingHealth > plantComponent.getPlantHealth());

    }

    @Test
    void testBatAttack() {

        attackPatternComponent = spy(getAttackPatternComponent(EntityType.BAT));

        entity.addComponent(attackPatternComponent);
        entity.addComponent(new CombatStatsComponent(10, 10));
        entity.create();

        Entity target = new Entity(EntityType.PLAYER);

        CombatStatsComponent combatStatsComponent = new CombatStatsComponent(100, 0);
        target.addComponent(combatStatsComponent);
        target.create();

        when(interactionDetector.getEntitiesInRange()).thenReturn(List.of(target));

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", target);
        entity.update();

        // check attack starts before shoot
        verify(attackPatternComponent, times(1)).attack();

        assertEquals(target.getComponent(CombatStatsComponent.class).getHealth(), 90);

        when(interactionDetector.getEntitiesInRange()).thenReturn(new ArrayList<>());

        // wait for delay
        when(gameTime.getTime()).thenReturn(1000L);

        entity.update();

    }


    private AttackPatternComponent getAttackPatternComponent(EntityType type) {
        if (type == EntityType.DRAGONFLY) {
            return new DragonflyAttackPattern(1f, this::createMockProjectile);
        } else if (type == EntityType.OXYGEN_EATER) {
            return new OxygenEaterAttackPattern(1f, this::createMockProjectile);
        }  else if (type == EntityType.BAT) {
        return new BatAttackPattern(1f);
    }

        return new AttackPatternComponent(1f);
    }

    private Entity createMockProjectile() {
        Entity projectile = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ProjectileComponent(1f));

        projectile.create();

        return projectile;
    }
}
