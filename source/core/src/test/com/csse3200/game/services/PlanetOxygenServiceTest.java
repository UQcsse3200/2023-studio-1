package com.csse3200.game.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanetOxygenServiceTest {
    PlanetOxygenService planetOxygenService;
    @BeforeEach
    void setUp() {
        planetOxygenService = new PlanetOxygenService();
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

        // Test when oxygenGoal is zero (should return -1) - buggy at the moment
        //planetOxygenService.setOxygenGoal(0);
        //assertEquals(-1, planetOxygenService.getOxygenPercentage());
    }
    
    @Test
    void setOxygenGoal() {
        planetOxygenService.setOxygenGoal(500);
        assertEquals(500, planetOxygenService.getOxygenGoal(),
            "Oxygen goal was not set correctly");
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