package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
class MultiDropComponentTest {
    private Entity entity;
    private TimeService timeService;
    private int initialEntityCount;

    private Entity createDummyItem() {
        return new Entity(EntityType.Item)
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemActions())
                .addComponent(new ItemComponent("dummy", ItemType.FERTILISER,
                        new Texture("images/dont_delete_test_image.png")));
    }

    private Entity createDummyEntity() {
        Entity dummyPlayer = new Entity();
        entity = new Entity();

        //Set up tamable component
        TamableComponent tamableComponent = new TamableComponent(dummyPlayer, 2, 1.1, "AFood");
        entity.addComponent(tamableComponent);

        //Set up multi-drop component
        List<SingleDropHandler> singleDropHandlers = new ArrayList<>();

        //Drop handler for untamed, 1 trigger to next drop, listen on entity
        singleDropHandlers.add(new SingleDropHandler(this::createDummyItem, 1,
                entity.getEvents()::addListener, "untamed-entity-1-trigger", false));

        //Drop handler for untamed, 2 triggers to next drop, listen on entity
        singleDropHandlers.add(new SingleDropHandler(this::createDummyItem, 2,
                entity.getEvents()::addListener, "untamed-entity-2-trigger", false));
        entity.addComponent(new MultiDropComponent(singleDropHandlers, false));

        //Drop handler for tamed, 1 trigger to next drop, listen on time service
        singleDropHandlers.add(new SingleDropHandler(this::createDummyItem, 1,
                timeService.getEvents()::addListener, "hourUpdate", true));

        entity.addComponent(new MultiDropComponent(singleDropHandlers, false));

        entity.create();
        return entity;
    }

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerGameArea(new GameArea() {
            @Override
            public void create() {
                //Do nothing
            }

            @Override
            public Entity getPlayer() {
                return null;
            }

            @Override
            public ClimateController getClimateController() {
                return null;
            }

            @Override
            public Entity getTractor() {
                return null;
            }

            @Override
            public GameMap getMap() {
                return null;
            }
        });
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        timeService = new TimeService();
        ServiceLocator.registerTimeService(timeService);
        initialEntityCount = ServiceLocator.getEntityService().getSize();
    }

    @Test
    void checkItemDropsUntamed() {
        Entity entity = createDummyEntity();
        assertEquals(initialEntityCount, ServiceLocator.getEntityService().getSize());

        //Drop should not occur as requires two triggers
        entity.getEvents().trigger("untamed-entity-2-trigger");
        assertEquals(initialEntityCount, ServiceLocator.getEntityService().getSize());

        //Drop should not occur as entity not tamed
        timeService.setHour(1);
        assertEquals(initialEntityCount, ServiceLocator.getEntityService().getSize());

        //Drop should occur
        entity.getEvents().trigger("untamed-entity-1-trigger");
        assertEquals(initialEntityCount + 1, ServiceLocator.getEntityService().getSize());

    }

    @Test
    void checkItemDropsTamed() {
        Entity entity = createDummyEntity();
        assertEquals(initialEntityCount, ServiceLocator.getEntityService().getSize());

        entity.getComponent(TamableComponent.class).setTame(true);

        //Drop should not occur as requires two triggers
        entity.getEvents().trigger("untamed-entity-2-trigger");
        assertEquals(initialEntityCount, ServiceLocator.getEntityService().getSize());

        //Drop should occur as entity tamed
        timeService.setHour(1);
        assertEquals(initialEntityCount + 1, ServiceLocator.getEntityService().getSize());

        //Drop should occur
        entity.getEvents().trigger("untamed-entity-1-trigger");
        assertEquals(initialEntityCount + 2, ServiceLocator.getEntityService().getSize());
    }

    @Test
    void checkItemDropRate() {
        Entity entity = createDummyEntity();

        //Drop should not occur as requires two triggers
        entity.getEvents().trigger("untamed-entity-2-trigger");
        assertEquals(initialEntityCount, ServiceLocator.getEntityService().getSize());

        //Drop should occur as second trigger
        entity.getEvents().trigger("untamed-entity-2-trigger");
        assertEquals(initialEntityCount + 1, ServiceLocator.getEntityService().getSize());
    }
}
