package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.PlayerHungerService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class HungerComponentTest {
    HungerComponent hungerComponent;
    PlayerHungerService mockPlayerHungerService;
    EventHandler mockEventHandler;
    int INITIAL_HUNGER_LEVEL = 30;
    @BeforeEach
    void beforeEach() {
        hungerComponent = new HungerComponent(INITIAL_HUNGER_LEVEL);

        mockPlayerHungerService = mock(PlayerHungerService.class);
        ServiceLocator.registerPlayerHungerService(mockPlayerHungerService);

        mockEventHandler = mock(EventHandler.class);
        when(ServiceLocator.getPlayerHungerService().getEvents()).thenReturn(mockEventHandler);
    }

    @Test
    void shouldSetGetHunger() {

        assertEquals(INITIAL_HUNGER_LEVEL, hungerComponent.getHungerLevel());

        hungerComponent.setHungerLevel(5);
        assertEquals(5, hungerComponent.getHungerLevel());
    }

    @Test
    void checkIfStarving() {
        hungerComponent.setHungerLevel(100);
        assertTrue(hungerComponent.checkIfStarving());

        hungerComponent.setHungerLevel(66);
        assertFalse(hungerComponent.checkIfStarving());
    }

    @Test
    void testIncreaseHungerLevel() {
        hungerComponent.increaseHungerLevel(1);
        assertEquals(INITIAL_HUNGER_LEVEL + 1, hungerComponent.getHungerLevel());

        hungerComponent.setHungerLevel(INITIAL_HUNGER_LEVEL);
        hungerComponent.increaseHungerLevel(-1);
        assertEquals(INITIAL_HUNGER_LEVEL - 1, hungerComponent.getHungerLevel());

        hungerComponent.setHungerLevel(0);
        hungerComponent.increaseHungerLevel(-1);
        assertEquals(0, hungerComponent.getHungerLevel());

        hungerComponent.setHungerLevel(100);
        hungerComponent.increaseHungerLevel(1);
        assertEquals(100, hungerComponent.getHungerLevel());
    }
}
