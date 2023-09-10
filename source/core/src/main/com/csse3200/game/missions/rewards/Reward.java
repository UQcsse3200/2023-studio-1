package com.csse3200.game.missions.rewards;

/**
 * The `Reward` class is an abstract class representing an in-game reward which
 * can be collected as a result of
 * completing an Mission **(v1 only!)** or milestone **(v2 only!)**. The public
 * `isCollected()` method is provided by
 * default, and returns `true` iff the reward’s `collect()` method has been
 * called.
 */
public abstract class Reward {

    private boolean isCollected;

    public Reward() {
        isCollected = false;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected() {
        isCollected = true;
    }

    public abstract void collect();
}
