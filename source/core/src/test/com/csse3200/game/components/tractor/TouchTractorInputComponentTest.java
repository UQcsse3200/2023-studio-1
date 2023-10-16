package com.csse3200.game.components.tractor;


import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import com.badlogic.gdx.Input;

@ExtendWith(GameExtension.class)
public class TouchTractorInputComponentTest {
    @Test
    void constructor() {
        TouchTractorInputComponent inputComponent = spy(TouchTractorInputComponent.class);
        assertEquals(5, inputComponent.getPriority());
    }


    @Test
    void shouldHandleKeyDown(){
        TouchTractorInputComponent inputComponent = spy(TouchTractorInputComponent.class);
        Entity tractor = new Entity();

        tractor.addComponent(mock(TractorActions.class));
        tractor.addComponent(inputComponent);

        assertFalse(inputComponent.keyDown(1));
        assertTrue(inputComponent.keyDown(Input.Keys.UP));
        assertTrue(inputComponent.keyDown(Input.Keys.LEFT));
        assertTrue(inputComponent.keyDown(Input.Keys.DOWN));
        assertTrue(inputComponent.keyDown(Input.Keys.RIGHT));
    }

    @Test
    void shouldHandleKeyUp(){
        TouchTractorInputComponent inputComponent = spy(TouchTractorInputComponent.class);
        Entity tractor = new Entity();

        tractor.addComponent(mock(TractorActions.class));
        tractor.addComponent(inputComponent);

        assertFalse(inputComponent.keyUp(1));
        assertFalse(inputComponent.keyUp(Input.Keys.UP));
        assertFalse(inputComponent.keyUp(Input.Keys.LEFT));
        assertFalse(inputComponent.keyUp(Input.Keys.DOWN));
        assertFalse(inputComponent.keyUp(Input.Keys.RIGHT));
        assertTrue(inputComponent.keyUp(Input.Keys.W));
        assertTrue(inputComponent.keyUp(Input.Keys.A));
        assertTrue(inputComponent.keyUp(Input.Keys.S));
        assertTrue(inputComponent.keyUp(Input.Keys.D));
    }
}