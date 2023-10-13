package com.csse3200.game.entities.factories;

import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemFactoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void createBaseItem() {
        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        Entity baseItem = ItemFactory.createBaseItem();
        assert baseItem != null;
        assert baseItem.getComponent(ItemActions.class) != null;
        assert baseItem.getComponent(HitboxComponent.class) != null;
        assert baseItem.getComponent(PhysicsComponent.class) != null;
    }
}
