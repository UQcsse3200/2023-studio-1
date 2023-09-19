package com.csse3200.game.services;

import com.csse3200.game.events.EventHandler;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanetOxygenServiceTest {
    PlanetOxygenService planetOxygenService;
    EventHandler eventHandler;
    @BeforeEach
    void setUp() {
        TimeService timeService = mock(TimeService.class);
        ServiceLocator.registerTimeService(timeService);
        eventHandler = new EventHandler();
        when(timeService.getEvents()).thenReturn(eventHandler);

        planetOxygenService = new PlanetOxygenService();
    }

    @AfterEach
    void clear() {
        ServiceLocator.clear();
    }
    
    @Test
    void addGetOxygen() {
        planetOxygenService.addOxygen(250);
        assertEquals(planetOxygenService.getDefaultInitialOxygen() + 250,
                planetOxygenService.getOxygen(), "250kgs not added correctly");
    }
    
    @Test
    void addRemoveGetOxygen() {
        planetOxygenService.addOxygen(375);
        planetOxygenService.removeOxygen(200);
        assertEquals(planetOxygenService.getDefaultInitialOxygen() + 175,
                planetOxygenService.getOxygen(), "200kgs not removed correctly");
    }
    
    @Test
    void getOxygenPercentage() {
        // Test when oxygenGoal is positive
        planetOxygenService.setOxygenGoal(1000);
        planetOxygenService.addOxygen(400);

        // Account for the initial 100 kilograms of oxygen already present.
        assertEquals(50,planetOxygenService.getOxygenPercentage(),
                "Oxygen percentage calculation is incorrect");
    }
    
    @Test
    void setOxygenGoal() {
        // Test when oxygen goal is set correctly
        planetOxygenService.setOxygenGoal(500);
        assertEquals(500, planetOxygenService.getOxygenGoal(),
            "Oxygen goal was not set correctly");

        // Test when oxygen goal is set to 0
        try {
            planetOxygenService.setOxygenGoal(0);
            fail("Should have caught the IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Test when oxygen goal is set to a negative number
        try {
            planetOxygenService.setOxygenGoal(-20);
            fail("Should have caught the IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    void getEvents() {
        assertNotNull(planetOxygenService.getEvents(),
            "Event handler should not be null");
    }
    
    @Test
    void update() {
        // Do this when entity oxygen levels can be tracked.
    }
}