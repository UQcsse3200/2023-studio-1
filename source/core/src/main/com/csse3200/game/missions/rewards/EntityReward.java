package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class EntityReward extends Reward {

    private final List<Entity> rewardEntities;


    public EntityReward(List<Entity> rewardEntities) {
        super();
        this.rewardEntities = rewardEntities;
    }

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
