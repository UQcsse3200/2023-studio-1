package com.csse3200.game.missions.rewards;

import java.util.List;

/**
 * The MultiReward is a Reward that can be added to a Quest in order to have multiple types
 * of Reward in one class.
 */
public class MultiReward extends Reward {
    /**
     * List of Rewards to be collected on collection of this MultiReward.
     */
    private List<Reward> rewards;

    /**
     * A MultiReward which allows multiple types of Rewards to be added to a Quest.
     * @param rewards - the rewards to be collected on collect() of the MultiReward
     */
    public MultiReward(List<Reward> rewards) {
        super();
        this.rewards = rewards;
    }

    /**
     * When called this method will call collect() on each of the rewards in the list.
     * Note - they are called in the order they are specified in the List.
     */
    @Override
    public void collect() {
        setCollected();
        for (Reward reward : this.rewards) {
            reward.collect();
        }
    }
}
