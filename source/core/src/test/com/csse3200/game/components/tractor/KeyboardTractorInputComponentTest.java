package com.csse3200.game.components.tractor;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
}
