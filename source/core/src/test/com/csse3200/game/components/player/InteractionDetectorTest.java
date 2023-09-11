package com.csse3200.game.components.player;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class InteractionDetectorTest {

    private InteractionDetector interactor;
    private Entity interactionEntity;
    private Fixture interactionFixture;

    @BeforeEach
    public void setUp() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        interactor = spy(new InteractionDetector(1.0f));  // Replace 1.0f with your desired range.
        interactionEntity = new Entity().addComponent(interactor)
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent());
        interactionEntity.setPosition(0, 0);
        interactionEntity.setScale(0,0); // sets scale to 0 so getCenterPosition == setPosition. easier to test
        interactionEntity.create();
        interactionFixture = interactor.getFixture();

    }

    @Test
    public void testGetEntitiesInRange() {
        // This test ensures event listeners work as intended, and entities in range are detected

        Entity entity1 = makeEntity();
        Entity entity2 = makeEntity();
        Fixture entity1Fixture = entity1.getComponent(ColliderComponent.class).getFixture();
        Fixture entity2Fixture = entity2.getComponent(ColliderComponent.class).getFixture();

        // Test event listeners work as it inherits from HitBoxComponent so collisions are assumed to work as intended.
        // trigger collisionStart
        interactionEntity.getEvents().trigger("collisionStart", interactionFixture, entity1Fixture);
        interactionEntity.getEvents().trigger("collisionStart", interactionFixture, entity2Fixture);

        // Check entities are added to entitiesInRange
        assertTrue(interactor.getEntitiesInRange().contains(entity1));
        assertTrue(interactor.getEntitiesInRange().contains(entity2));

        // trigger collisionEnd
        interactionEntity.getEvents().trigger("collisionEnd", interactionFixture, entity1Fixture);

        // Check entity1 no longer in range
        assertFalse(interactor.getEntitiesInRange().contains(entity1));
        assertTrue(interactor.getEntitiesInRange().contains(entity2));

        // trigger collision with fixture != Interaction fixture
        Fixture colliderFixture = interactionEntity.getComponent(ColliderComponent.class).getFixture();
        interactionEntity.getEvents().trigger("collisionStart", colliderFixture, entity1Fixture);
        interactionEntity.getEvents().trigger("collisionEnd", colliderFixture, entity2Fixture);

        // Check no changes made
        assertFalse(interactor.getEntitiesInRange().contains(entity1));
        assertTrue(interactor.getEntitiesInRange().contains(entity2));

        // Check empty list returned
        interactionEntity.getEvents().trigger("collisionEnd", interactionFixture, entity1Fixture);
        interactionEntity.getEvents().trigger("collisionEnd", interactionFixture, entity2Fixture);
        assertTrue(interactor.getEntitiesInRange().isEmpty());
    }

    @Test
    public void testGetEntitiesTowardsDirection() {
        Entity entity1 = makeEntity(new Vector2(0f, 1f)); // up direction
        Entity entity2 = makeEntity(new Vector2(-1f, 0f)); // left direction
        Entity entity3 = makeEntity(new Vector2(0, -1)); // down direction
        Entity entity4 = makeEntity(new Vector2(1, 0)); // right direction
        Entity entity5 = makeEntity(new Vector2(1, 0.5f)); // roughly right direction

        List<Entity> entities = new ArrayList<>();

        // Previous test already checks event listeners, mock getEntitiesInRange
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);
        entities.add(entity5);

        // i am in pain from this line, if i use thenReturn it returns the same instance even if im making a copy >:(
        when(interactor.getEntitiesInRange()).thenAnswer(x -> new ArrayList<>(entities));

        // assert up direction only contains entity1
        assertEquals(1, interactor.getEntitiesTowardsDirection(DirectionUtils.UP).size());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.UP).contains(entity1));

        // assert left direction only contains entity2
        assertEquals(1, interactor.getEntitiesTowardsDirection(DirectionUtils.LEFT).size());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.LEFT).contains(entity2));

        // assert down direction only contains entity3
        assertEquals(1, interactor.getEntitiesTowardsDirection(DirectionUtils.DOWN).size());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.DOWN).contains(entity3));

        // assert right direction only contains entity4,5
        assertEquals(2, interactor.getEntitiesTowardsDirection(DirectionUtils.RIGHT).size());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.RIGHT).contains(entity4));
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.RIGHT).contains(entity5));

        // handle empty
        when(interactor.getEntitiesInRange()).thenReturn(Collections.emptyList());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.UP).isEmpty());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.DOWN).isEmpty());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.LEFT).isEmpty());
        assertTrue(interactor.getEntitiesTowardsDirection(DirectionUtils.RIGHT).isEmpty());

    }

    @Test
    public void testGetEntitiesTowardsPosition() {
        Entity entity1 = makeEntity(new Vector2(0f, 1f)); // up direction
        Entity entity2 = makeEntity(new Vector2(-1f, 0f)); // left direction
        Entity entity3 = makeEntity(new Vector2(0, -1)); // down direction
        Entity entity4 = makeEntity(new Vector2(1, 0)); // right direction
        Entity entity5 = makeEntity(new Vector2(1, 0.5f)); // roughly right direction

        // Test a variety of positions of each quadrant
        Vector2 upPosition = new Vector2(0, 1);
        Vector2 leftPosition = new Vector2(-10, -9);
        Vector2 downPosition = new Vector2(-0.1f, -0.15f);
        Vector2 rightPosition = new Vector2(15f, 12f);

        List<Entity> entities = new ArrayList<>();

        // Previous test already checks event listeners, mock getEntitiesInRange
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);
        entities.add(entity5);

        when(interactor.getEntitiesInRange()).thenAnswer(x -> new ArrayList<>(entities));

        // assert up position only contains entity1
        assertEquals(1, interactor.getEntitiesTowardsPosition(upPosition).size());
        assertTrue(interactor.getEntitiesTowardsPosition(upPosition).contains(entity1));

        // assert left position only contains entity2
        assertEquals(1, interactor.getEntitiesTowardsPosition(leftPosition).size());
        assertTrue(interactor.getEntitiesTowardsPosition(leftPosition).contains(entity2));

        // assert down position only contains entity3
        assertEquals(1, interactor.getEntitiesTowardsPosition(downPosition).size());
        assertTrue(interactor.getEntitiesTowardsPosition(downPosition).contains(entity3));

        // assert right position only contains entity4,5
        assertEquals(2, interactor.getEntitiesTowardsPosition(rightPosition).size());
        assertTrue(interactor.getEntitiesTowardsPosition(rightPosition).contains(entity4));
        assertTrue(interactor.getEntitiesTowardsPosition(rightPosition).contains(entity5));

        // handle empty
        when(interactor.getEntitiesInRange()).thenReturn(Collections.emptyList());
        assertTrue(interactor.getEntitiesTowardsPosition(upPosition).isEmpty());
        assertTrue(interactor.getEntitiesTowardsPosition(downPosition).isEmpty());
        assertTrue(interactor.getEntitiesTowardsPosition(leftPosition).isEmpty());
        assertTrue(interactor.getEntitiesTowardsPosition(rightPosition).isEmpty());
    }

    @Test
    public void testGetNearest() {
        Entity entity1 = makeEntity(new Vector2(12f, 10f));
        Entity entity2 = makeEntity(new Vector2(-1f, 0f));
        Entity entity3 = makeEntity(new Vector2(0f, -1f));

        List<Entity> entities = new ArrayList<>();

        entities.add(entity1);
        entities.add(entity2);

        assertEquals(1, interactor.getNearest(entities).size());
        assertTrue(interactor.getNearest(entities).contains(entity2));

        // if two entities with same distance, just return whichever
        entities.add(entity3);
        assertEquals(1, interactor.getNearest(entities).size());
        assertTrue(interactor.getNearest(entities).contains(entity2) || interactor.getNearest(entities).contains(entity3));
    }

    @Test
    public void testGetSuitableEntitiesForFood() {
        Entity tamableEntity1ToTheRight = spy(makeEntity(new Vector2(1, 0)));
        TamableComponent entity1TameComponentMock = mock(TamableComponent.class);
        when(tamableEntity1ToTheRight.getComponent(TamableComponent.class)).thenReturn(entity1TameComponentMock);
        when(entity1TameComponentMock.isTamed()).thenReturn(false);

        Entity tamableEntity2ToTheLeft = spy(makeEntity(new Vector2(-1, 0)));
        TamableComponent entity2TameComponentMock = mock(TamableComponent.class);
        when(tamableEntity2ToTheLeft.getComponent(TamableComponent.class)).thenReturn(entity2TameComponentMock);
        when(entity2TameComponentMock.isTamed()).thenReturn(false);

        Entity tamableEntity3AlreadyTamed = spy(makeEntity(new Vector2(0, 1))); // in up direction
        TamableComponent entity3TameComponentMock = mock(TamableComponent.class);
        when(tamableEntity3AlreadyTamed.getComponent(TamableComponent.class)).thenReturn(entity3TameComponentMock);
        when(entity3TameComponentMock.isTamed()).thenReturn(true);

        Entity nonTamableEntity4 = makeEntity(new Vector2(-0.5f, 0));

        List<Entity> entities = new ArrayList<>();
        entities.add(tamableEntity1ToTheRight);
        entities.add(tamableEntity2ToTheLeft);
        entities.add(tamableEntity3AlreadyTamed);
        entities.add(nonTamableEntity4);

        when(interactor.getEntitiesInRange()).thenAnswer(x -> new ArrayList<>(entities));

        // Check behaviour when only one entity towards position, and tamable entity
        assertEquals(1, interactor.getSuitableEntities(ItemType.FOOD, new Vector2(1, 0)).size());
        assertTrue(interactor.getSuitableEntities(ItemType.FOOD, new Vector2(1, 0)).contains(tamableEntity1ToTheRight));

        // Check returns tamable entity even non-tamable entity is nearer in that direction
        assertEquals(1, interactor.getSuitableEntities(ItemType.FOOD, new Vector2(-1, 0)).size());
        assertTrue(interactor.getSuitableEntities(ItemType.FOOD, new Vector2(-1, 0)).contains(tamableEntity2ToTheLeft));

        // Check doesn't return already tamed entity
        assertTrue(interactor.getSuitableEntities(ItemType.FOOD, new Vector2(0, 1)).isEmpty());
        assertTrue(interactor.getSuitableEntities(ItemType.FOOD, new Vector2(-1, 0)).contains(tamableEntity2ToTheLeft));

        // Set position to nearest tamable entity to right
        tamableEntity2ToTheLeft.setPosition(0.5f, 0f);

        // Check that nearest tamable entity is returned
        assertEquals(1, interactor.getSuitableEntities(ItemType.FOOD, new Vector2(1, 0)).size());
        assertTrue(interactor.getSuitableEntities(ItemType.FOOD, new Vector2(1, 0)).contains(tamableEntity2ToTheLeft));
    }

    @Test
    public void testGetSuitableForUnaddedItemType() {
        Entity entity1 = makeEntity(new Vector2(0f, 1f)); // up direction
        Entity entity2 = makeEntity(new Vector2(-1f, 0f)); // left direction
        Entity entity3 = makeEntity(new Vector2(0, -1)); // down direction
        Entity entity4 = makeEntity(new Vector2(1, 0)); // right direction
        Entity entity5 = makeEntity(new Vector2(1, 0.5f)); // roughly right direction

        List<Entity> entities = new ArrayList<>();

        // Previous test already checks event listeners, mock getEntitiesInRange
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);
        entities.add(entity5);

        when(interactor.getEntitiesInRange()).thenAnswer(x -> new ArrayList<>(entities));

        // check empty list for null item type
        assertTrue(interactor.getSuitableEntities(ItemType.HOE, new Vector2(0, 1)).isEmpty());
    }

    public Entity makeEntity() {
        Entity entity = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent());
        entity.create();
        return entity;
    }

    public Entity makeEntity(Vector2 position) {
        Entity entity = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent());
        entity.setPosition(position);
        entity.setScale(0, 0); // sets scale to 0 so getCenterPosition == setPosition. easier to test
        entity.create();
        return entity;
    }
}
