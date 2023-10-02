package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;

import java.util.List;

public class TriggerHostilesReward extends Reward {

    private final List<Entity> hostiles;

    public TriggerHostilesReward(List<Entity> hostiles) {
        super();
        this.hostiles = hostiles;
    }

    @Override
    public void collect() {
        setCollected();

        GameArea gameArea = ServiceLocator.getGameArea();
        for (Entity enemy : hostiles) {
            GridPoint2 playerPos = gameArea.getMap().vectorToTileCoordinates(gameArea.getPlayer().getCenterPosition());

            GridPoint2 lower = new GridPoint2(playerPos.x - 10, playerPos.y - 10);
            GridPoint2 upper = new GridPoint2(playerPos.x + 10, playerPos.y + 10);

            GridPoint2 spawnPos = RandomUtils.random(lower, upper);

            while (!gameArea.getMap().getTile(spawnPos).isTraversable()) {
                spawnPos = RandomUtils.random(lower, upper);
            }

            gameArea.spawnEntityAt(enemy, spawnPos, true, true);
        }

        // TODO - Trigger hostiles to spawn in the world (to be implemented by team 4)
    }

}
