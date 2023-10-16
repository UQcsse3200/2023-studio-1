package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class BlinkComponentTest {
    private BlinkComponent blinkComponent;
    private Entity testEntity;

    @BeforeEach
    public void setUp() {
        // Mock game time
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        when(gameTime.getDeltaTime()).thenReturn(0.1f);

        blinkComponent = spy(new BlinkComponent());
        testEntity = new Entity()
                .addComponent(blinkComponent);

        testEntity.create();


    }

    @Test
    void testDamageBlink() {
        // test listens to trigger starts damage blink
        testEntity.getEvents().trigger("hit", mock(Entity.class));
        assertEquals(Color.WHITE, blinkComponent.getColor());
        assertTrue(blinkComponent.isBlinking());

        // test that opacity changes gradually to and from min opacity
        testEntity.update();
        assertEquals(1f, blinkComponent.getColor().a);

        testEntity.update();
        assertEquals(0.75f, blinkComponent.getColor().a);

        testEntity.update();
        assertEquals(0.5f, blinkComponent.getColor().a);

        testEntity.update();
        assertEquals(0.75f, blinkComponent.getColor().a);

        testEntity.update();
        assertEquals(1f, blinkComponent.getColor().a);

        // after 0.4, check no longer blinking
        testEntity.update();
        assertFalse(blinkComponent.isBlinking());
        assertEquals(1f, blinkComponent.getColor().a);

        // check update does not affect opacity when not blinking
        testEntity.update();
        assertFalse(blinkComponent.isBlinking());
        assertEquals(1f, blinkComponent.getColor().a);
    }
}
