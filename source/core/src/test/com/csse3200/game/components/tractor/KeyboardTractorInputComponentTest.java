package com.csse3200.game.components.tractor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.SoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class KeyboardTractorInputComponentTest {
    @BeforeEach
    void setup() {
        Entity tractor = TractorFactory.createTractor();
    }
    @Test
    void getWalkDirection() {
        KeyboardTractorInputComponent inputComponent = spy(KeyboardTractorInputComponent.class);
        assertEquals(inputComponent.getWalkDirection(), Vector2.Zero.cpy());
    }

    @Test
    void setWalkDirection() {
        KeyboardTractorInputComponent inputComponent = spy(KeyboardTractorInputComponent.class);
        Vector2 newWalkDirection = new Vector2(1, 1);
        inputComponent.setWalkDirection(newWalkDirection);
        verify(inputComponent).setWalkDirection(newWalkDirection);
        assertEquals(inputComponent.getWalkDirection(), newWalkDirection);
    }

    @Test
    void constructor() {
        KeyboardTractorInputComponent inputComponent = spy(KeyboardTractorInputComponent.class);
        assertEquals(inputComponent.getPriority(), 5);
    }

    @Test
    void shouldHandleKeyDown(){
        KeyboardTractorInputComponent inputComponent = spy(KeyboardTractorInputComponent.class);
        Entity tractor = new Entity();

        tractor.addComponent(mock(TractorActions.class));
        tractor.addComponent(inputComponent);

        //mock tractor so that do return on tractor is muted return false
        doReturn(false).when(tractor.getComponent(TractorActions.class)).isMuted();

        assertFalse(inputComponent.keyDown(1));
        assertTrue(inputComponent.keyDown(Input.Keys.W));
        assertTrue(inputComponent.keyDown(Input.Keys.A));
        assertTrue(inputComponent.keyDown(Input.Keys.S));
        assertTrue(inputComponent.keyDown(Input.Keys.D));
        assertTrue(inputComponent.keyDown(Input.Keys.F));
        assertTrue(inputComponent.keyDown(Input.Keys.T));
        assertTrue(inputComponent.keyDown(Input.Keys.NUM_1));
        assertTrue(inputComponent.keyDown(Input.Keys.NUM_2));
        assertTrue(inputComponent.keyDown(Input.Keys.NUM_3));
    }
}