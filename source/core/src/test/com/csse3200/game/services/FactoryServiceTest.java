package com.csse3200.game.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FactoryServiceTest {

    @Test
    void getFactories() {
        assertNotNull(FactoryService.getNpcFactories());
        assertNotNull(FactoryService.getQuests());
    }
}
