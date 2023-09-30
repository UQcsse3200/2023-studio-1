package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.ServiceLocator;


public abstract class Quest extends Mission {

	/**
	 * True if expiry of {@link Quest} should result in a game-over, false otherwise
	 */
	private final boolean isMandatory;

	/**
	 * The duration of the {@link Quest} once it has been accepted before it expires
	 */
	private final int duration;

	/**
	 * The amount of time remaining before the {@link Quest} expires
	 */
	private int timeToExpiry;

	/**
	 * Represents whether the {@link Quest} should be able to expire
	 */
	private final boolean canExpire;

	/**
	 * The {@link Reward} to be collected once the {@link Quest} has been completed
	 */
	private final Reward reward;

	/**
	 * Creates a {@link Quest} with the given {@link String} name, specifying the {@link Reward} for completion.
	 * {@link Quest}s created using this constructor cannot expire.
	 *
	 * @param name The {@link String} name of the {@link Quest}, visible to the player in-game
	 * @param reward The {@link Reward} which can be collected upon completion of the {@link Quest}
	 */
	protected Quest(String name, Reward reward) {
		super(name);
		this.reward = reward;
		this.canExpire = false;

		this.duration = -1;
		this.timeToExpiry = 0;

		this.isMandatory = false;
	}

	/**
	 * Creates a {@link Quest} with the given {@link String} name, specifying the {@link Reward} for completion, the
	 * integer duration of the {@link Quest} (the number of hours before the {@link Quest} expires), and whether the
	 * {@link Quest} is mandatory. {@link Quest}s created using this constructor will expire after the specified time
	 * limit.
	 *
	 * @param name The {@link String} name of the {@link Quest}, visible to the player in-game
	 * @param reward The {@link Reward} which can be collected upon completion of the {@link Quest}
	 * @param expiryDuration An integer value representing the number of hours before this {@link Quest} should expire
	 * @param isMandatory A boolean value representing whether the {@link Quest} is mandatory. Mandatory {@link Quest}s
	 *                    will result in a game over if it is not completed before it expires.
	 */
	protected Quest(String name, Reward reward, int expiryDuration, boolean isMandatory) {
		super(name);
		this.reward = reward;
		this.duration = expiryDuration;
		this.isMandatory = isMandatory;

		this.timeToExpiry = this.duration;
		this.canExpire = true;
	}

	/**
	 * Decrements the duration to expiry of the quest by 1, if the {@link Quest} can expire.
	 */
	public void updateExpiry() {
		if (canExpire && !isCompleted() && --timeToExpiry <= 0) {
			timeToExpiry = 0;
		}
	}

	/**
	 * Returns a boolean value representing whether the quest has expired. If the {@link Quest} does not expire, then
	 * this method will always return false.
	 * @return True if the {@link Quest} can expire and has expired, false otherwise.
	 */
	public boolean isExpired() {
		return canExpire && timeToExpiry <= 0;
	}

	/**
	 * Determines whether the quest is mandatory or not
	 * @return boolean value representing whether the quest is mandatory
	 */
	public boolean isMandatory() {
		return isMandatory;
	}

	/**
	 * Returns a boolean value representing if this quest's rewards has been collected yet.
	 * @return True if the reward has been collected, false otherwise.
	 */
	public boolean isRewardCollected() {
		return reward.isCollected();
	}

	/**
	 * Collects the reward associated with this {@link Quest}. If the reward has already been collected, or if the
	 * {@link Mission} has not been completed, this method will do nothing.
	 */
	public void collectReward() {
		if (isCompleted() && !reward.isCollected()) {
			reward.collect();
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(),
					getName());
		}
	}

	/**
	 * Resets the {@link Quest}'s time to expiry back to the original expiry duration, and calls the
	 * {@link Quest}'s {@link #resetState()} method. This method only calls {@link #resetState()} if it cannot expire.
	 */
	public void resetExpiry() {
		if (canExpire) {
			timeToExpiry = duration;
		}
		resetState();
	}

	/**
	 * Resets the internal state of the {@link Quest}, to what it was before being accepted/modified.
	 */
	protected abstract void resetState();

	public void write(Json json) {
		json.writeObjectStart("Quest");
		json.writeValue("name", getName());
		json.writeValue("expiry", timeToExpiry);
		json.writeValue("progress", getProgress());
		json.writeValue("collected", reward.isCollected());
		json.writeObjectEnd();
	}

	public void read(JsonValue jsonValue) {
		timeToExpiry = jsonValue.getInt("expiry");
		reward.read(jsonValue);
		readProgress(jsonValue.get("progress"));
	}
}
