package com.csse3200.game.components.tractor;


import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;


@ExtendWith(GameExtension.class)
public class TouchTractorInputComponentTest {
    @Test
    void constructor() {
        TouchTractorInputComponent inputComponent = spy(TouchTractorInputComponent.class);
        assertEquals(inputComponent.getPriority(), 5);
    }
}