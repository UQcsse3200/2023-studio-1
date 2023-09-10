package com.csse3200.game.components.npc;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.function.Supplier;

/**
 * Enables entity to drop a specified item at regular intervals
 */
public class PassiveDropComponent extends Component {
    /**
     * The function that is called to create the dropped item
     */
    private Supplier<Entity> dropItem;
    /**
     * Number of in-game hours until a drop should happen
     */
    private int timeToNextDrop;
    /**
     * Hours since the last drop
     */
    private int hoursPassed;
    /**
     * The game area to spawn the dropped item in
     */
    private GameArea gameArea;

    /**
     * Constructor for the PassiveDropComponent
     *
     * @param dropItem The item the entity will drop at dropRate
     * @param dropRate Number of drops per day (max 24). Rounding affects results.
     */
    public PassiveDropComponent(Supplier<Entity> dropItem, int dropRate) {
        this.dropItem = dropItem;
        this.timeToNextDrop = Math.max(1, Math.floorDiv(24, dropRate));
        this.hoursPassed = 0;
        this.gameArea = ServiceLocator.getGameArea();
    }

    @Override
    public void create() {
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::dropItems);
    }

    /**
     * Spawns the dropItem at the entity's location. Considers the drop rate of the item.
     */
    public void dropItems() {
        TamableComponent tamableComponent = this.entity.getComponent(TamableComponent.class);
        if (tamableComponent == null || !tamableComponent.isTamed()) {
            return;
        }

        hoursPassed += 1;
        if (hoursPassed == timeToNextDrop) {
            Entity item = dropItem.get();
            item.setPosition(entity.getPosition());
            gameArea.spawnEntity(item);
            hoursPassed = 0;
        }
    }
}
