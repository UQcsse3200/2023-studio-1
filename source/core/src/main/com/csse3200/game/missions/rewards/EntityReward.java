package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
    An Entity Reward class for when a player completes a Quest and receives reward entity as a result
 */
public class EntityReward extends Reward {

    private final List<Entity> rewardEntities; // The list of entities to be rewarded

    /**
        Constructor for the EntityReward class
        @param rewardEntities The list of entities to be rewarded
     */
    public EntityReward(List<Entity> rewardEntities) {
        super();
        this.rewardEntities = rewardEntities;
    }

    /**
        Collects the reward by spawning the reward entities at the player's position
     */
    @Override
    public void collect() {
        setCollected();
        Vector2 playerPosition = ServiceLocator.getGameArea().getPlayer().getPosition();
        GridPoint2 playerPositionGrid = new GridPoint2((int) playerPosition.x, (int) playerPosition.y);
        for (Entity entity : rewardEntities) {
            ServiceLocator.getGameArea().spawnEntityAt(entity, playerPositionGrid, true, true);
        }
    }
}
