package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
class PassiveDropComponentTest {
    private Entity entity;
    private PassiveDropComponent dropComponent;
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

    private Entity createDummyEntity(int dropRate, boolean isTamed) {
        dropComponent = new PassiveDropComponent(this::createDummyItem, dropRate);
        Entity dummyPlayer = new Entity();
        TamableComponent tamableComponent = new TamableComponent(dummyPlayer, 2, 1.1, "AFood");
        tamableComponent.setTame(isTamed);

        entity = new Entity()
                .addComponent(dropComponent)
                .addComponent(tamableComponent);

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
        });
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        timeService = new TimeService();
        ServiceLocator.registerTimeService(timeService);
        initialEntityCount = ServiceLocator.getEntityService().getSize();
    }

    @Test
    void checkItemDrops() {
        Entity entity = createDummyEntity(24, true);
        timeService.setHour(1); //Trigger one hourUpdate
        //Assert item has been dropped
        assertTrue(ServiceLocator.getEntityService().getSize() > initialEntityCount);
    }

    @Test
    void checkItemDropRate() {
        Entity entity = createDummyEntity(12, true);
        timeService.setHour(1); //Trigger one hourUpdate
        //Assert an item has not been dropped
        assertTrue(ServiceLocator.getEntityService().getSize() == initialEntityCount);
        timeService.setHour(1); //Trigger one hourUpdate
        //Assert item has been dropped
        assertTrue(ServiceLocator.getEntityService().getSize() > initialEntityCount);
    }

    @Test
    void doNothingOnUntamed() {
        Entity entity = createDummyEntity(24, false);
        timeService.setHour(1); //Trigger one hourUpdate
        //Assert nothing has happened
        assertTrue(ServiceLocator.getEntityService().getSize() == initialEntityCount);
    }

}
