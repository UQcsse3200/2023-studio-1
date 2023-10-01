package com.csse3200.game.missions.rewards;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import java.util.ArrayList;
import java.util.List;

class EntityRewardTest {

    private Entity e1, e2, e3;
    private List<Entity> entities1, entities2, entities3;
    private EntityReward r1, r2, r3;

    public class TestGameArea extends GameArea {
        Entity player = new Entity();

        @Override
        public void create() {
            // do nothing
        }

        @Override
        public Entity getPlayer() {
            return player;
        }

        @Override
        public ClimateController getClimateController() {
            return null;
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

        // Create some test entities
        e1 = new Entity();
        e2 = new Entity();
        e3 = new Entity();

        // Create lists of entities for rewards
        entities1 = new ArrayList<Entity>();
        entities1.add(e1);

        entities2 = new ArrayList<Entity>();
        entities2.add(e1);
        entities2.add(e2);

        entities3 = new ArrayList<Entity>();
        entities3.add(e1);
        entities3.add(e2);
        entities3.add(e3);

        // Create EntityReward objects
        r1 = new EntityReward(entities1);
        r2 = new EntityReward(entities2);
        r3 = new EntityReward(entities3);
    }

    @AfterEach
    void afterTest() {
        ServiceLocator.clear();
    }

    @Test
    public void testCollect() {
        // Ensure EntityReward is not collected initially
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
    }
}
