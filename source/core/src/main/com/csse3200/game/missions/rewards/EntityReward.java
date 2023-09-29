package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class EntityReward extends Reward {
    private static final GridPoint2 TRACTOR_SPAWN = new GridPoint2(15, 15);

    private final List<Entity> rewardEntities;


    public EntityReward(List<Entity> rewardEntities) {
        super();
        this.rewardEntities = rewardEntities;
    }

    @Override
    public void collect() {
        setCollected();
        Entity player = ServiceLocator.getGameArea().getPlayer();
        Entity tractor = TractorFactory.createTractor(player);
        ServiceLocator.getGameArea().spawnEntityAt(tractor, TRACTOR_SPAWN, true, true);
    }
}
