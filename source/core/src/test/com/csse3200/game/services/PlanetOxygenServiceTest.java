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
    
    }
    
    @Test
    void setOxygenGoal() {
    }
    
    @Test
    void getEvents() {
    }
    
    @Test
    void update() {
    }
}