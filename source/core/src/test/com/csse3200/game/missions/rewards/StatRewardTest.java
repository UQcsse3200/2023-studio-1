package com.csse3200.game.missions.rewards;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StatRewardTest {

    private StatReward r1, r2, r3;

    private class TestGameArea extends GameArea {
        Entity player = new Entity()
                .addComponent(new CombatStatsComponent(100, 10)); // like real player

        @Override
        public void create() {
            // do nothing
        }

        @Override
        public Entity getPlayer() {
            return player;
        }

        @Override
        public Entity getTractor() {
            return null;
        }

        @Override
        public GameMap getMap() {
            return null;
        }
    }


    @BeforeEach
    void beforeTest() {
        ServiceLocator.registerGameArea(new TestGameArea());

        r1 = new StatReward(0);
        r2 = new StatReward(-200);
        r3 = new StatReward(200);
    }

    @AfterEach
    void afterTest() {
        ServiceLocator.clear();
    }

    @Test
    public void noHealthReward() {
        CombatStatsComponent initialStats = ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class);
        r1.collect();
        initialStats.addHealth(0);
        assertEquals(initialStats, ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class));
        assertTrue(r1.isCollected());
    }

    @Test
    public void negativeHealthReward() {
        CombatStatsComponent initialStats = ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class);
        r2.collect();
        initialStats.addHealth(-200);
        assertEquals(initialStats, ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class));
        assertTrue(r2.isCollected());

    }

    @Test
    public void positiveHealthReward() {
        CombatStatsComponent initialStats = ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class);
        r3.collect();
        initialStats.addHealth(200);
        assertEquals(initialStats, ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class));
        assertTrue(r3.isCollected());
    }

}