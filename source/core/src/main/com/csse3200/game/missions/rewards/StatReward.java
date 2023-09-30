package com.csse3200.game.missions.rewards;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

/**
 * A StatReward that when called collect() on adds the specified health to the current player's health stat.
 * NOTE: The CombatStatsComponent has not been updated from original provided code, if extended on this class may
 *  need to be adjusted to suit.
 */
public class StatReward extends Reward {

    /**
     * Amount the current Player's health will be updated by when collect() method is called.
     */
    private final int healthToAdd;

    /**
     * Creates a new StatReward with a specified value to modify health by.
     * @param healthToAdd Amount player's health is to be changed by. Can also be negative if desired.
     */
    public StatReward(int healthToAdd) {
        super();
        this.healthToAdd = healthToAdd;
    }

    /**
     * When called will set this Reward as having been collected and will modify the player's health as specified.
     */
    @Override
    public void collect() {
        setCollected();

        Entity player = ServiceLocator.getGameArea().getPlayer();
        CombatStatsComponent playerStats = player.getComponent(CombatStatsComponent.class);
        playerStats.addHealth(healthToAdd);

    }
}
