package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.rewards.Reward;

public abstract class Quest extends Mission {

	private final boolean isMandatory;
	private int duration;
	private final Reward reward;

	/**
	 * Creates a {@link Mission} with the given {@link String} name, specifying the {@link Reward} for completion, the
	 * integer duration of the {@link Quest} (the number of hours before the {@link Quest} expires), and whether the
	 * {@link Quest} is mandatory.
	 *
	 * @param name The {@link String} name of the {@link Mission}, visible to the player in-game
	 * @param reward The {@link Reward} which can be collected upon completion of the {@link Quest}
	 * @param expiryDuration An integer value representing the number of hours before this {@link Quest} should expire
	 * @param isMandatory A boolean value representing whether the {@link Quest} is mandatory. Mandatory {@link Quest}s
	 *                    will result in a game over if it is not completed before it expires.
	 */
	public Quest(String name, Reward reward, int expiryDuration, boolean isMandatory) {
		super(name);
		this.reward = reward;
		this.duration = expiryDuration;
		this.isMandatory = isMandatory;
	}

	/**
	 * Decrements the duration to expiry of the quest by 1.
	 */
	public void updateExpiry() {
		duration--;
	}

	/**
	 * Returns a boolean value representing whether the quest has expired.
	 * @return True if the quest has expired, false otherwise.
	 */
	public boolean isExpired() {
		return duration <= 0;
	}

	/**
	 * Determines whether the quest is mandatory or not
	 * @return boolean value representing whether the quest is mandatory
	 */
	public boolean isMandatory() {
		return isMandatory;
	}

	/**
	 * Collects the reward associated with this {@link Quest}. If the reward has already been collected, or if the
	 * {@link Mission} has not been completed, this method will do nothing.
	 */
	public void collectReward() {
		if (!isCompleted() || !reward.isCollected()) {
			reward.collect();
		}
	}
}
