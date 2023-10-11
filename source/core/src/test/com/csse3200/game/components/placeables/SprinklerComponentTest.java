package com.csse3200.game.components.placeables;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
public class SprinklerComponentTest {
    private static Entity s1;
    private static Entity p1;

    @BeforeAll
    public static void setUp() {
        s1 = new Entity(EntityType.SPRINKLER)
                .addComponent(new SprinklerComponent());
        p1 = new Entity(EntityType.PUMP)
                .addComponent(new SprinklerComponent());

        assertNotNull(s1.getComponent(SprinklerComponent.class));
        assertNotNull(p1.getComponent(SprinklerComponent.class));

        p1.getComponent(SprinklerComponent.class).setPump();
    }

    @Test
    public void setPumpTest() {
        assertTrue(p1.getComponent(SprinklerComponent.class).getPump());
    }

    @Test
    public void sprinklerNotPowered() {
        assertFalse(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void pumpIsPowered() {
        assertTrue(p1.getComponent(SprinklerComponent.class).getPowered());
    }

    // next we test the hard things, like pump next to sprinkler powers it
}
