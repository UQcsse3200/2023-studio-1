package com.csse3200.game.missions;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.List;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.achievements.Achievement;
import com.csse3200.game.missions.achievements.PlantCropsAchievement;
import com.csse3200.game.missions.quests.FertiliseCropTilesQuest;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.missions.quests.QuestFactory;
import com.csse3200.game.missions.rewards.ItemReward;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;

public class MissionManager implements Json.Serializable {

	/**
	 * An enum storing all possible events that the {@link MissionManager}'s {@link EventHandler} should listen to and
	 * trigger. To add a listener to the {@link MissionManager}, create a new {@link MissionEvent} enum value, and add
	 * a listener for the {@link #name()} of the enum value.
	 */
	public enum MissionEvent {
		// Triggers when a mission is completed
		MISSION_COMPLETE,
		// Triggers when a new quest has been added to the mission manager
		NEW_QUEST,
		// Triggers when a quest expires
		QUEST_EXPIRED,
		// Triggers when a crop is planted, single String representing plant type is provided as argument
		PLANT_CROP,
		// Triggers when a crop is fertilised
		FERTILISE_CROP
	}

	/**
	 * The {@link MissionManager}'s {@link EventHandler}. {@link Mission}s should add listeners to
	 * this {@link EventHandler} to update their state, when said events are triggered by in-game
	 * interactions
	 */
	private final EventHandler events = new EventHandler();

	/**
	 * A {@link List} of {@link Quest}s which are currently active
	 */
	private final List<Quest> activeQuests = new ArrayList<>();

	/**
	 * A {@link List} of {@link Quest}s which the player may choose to accept by interacting with
	 * the missions NPC in-game
	 */
	private final List<Quest> selectableQuests = new ArrayList<>();

	/**
	 * An array of all in-game {@link Achievement}s
	 */
	private static final Achievement[] achievements = new Achievement[]{
			new PlantCropsAchievement("Plant President", 50),
			new PlantCropsAchievement("Crop Enjoyer", 200),
			new PlantCropsAchievement("Gardener of the Galaxy", 800)
	};

	/**
	 * Creates the mission manager, registered all game achievements and adds a listener for hourly updates
	 */
	public MissionManager() {
		ServiceLocator.getTimeService().getEvents().addListener("updateHour", this::updateActiveQuestTimes);
		for (Achievement mission : achievements) {
			mission.registerMission(events);
		}

		// Add initial quests - regardless of GameArea
		// This will be removed from the constructor at a later date
		selectableQuests.add(QuestFactory.createHaberHobbyist());
		selectableQuests.add(QuestFactory.createFertiliserFanatic());
	}

	/**
	 * Accepts a quest by adding it to the list of active quests in the game.  Also registers this quest in the game.
	 * If this {@link Quest} is in the {@link List} of selectable {@link Quest}s, then this method will also remove the
	 * {@link Quest} from the {@link List} of selectable {@link Quest}s.
	 * @param quest The {@link Quest} to be added and registered
	 */
	public void acceptQuest(Quest quest) {
		// Remove the quest from selectable quests if present
		selectableQuests.remove(quest);
		activeQuests.add(quest);
		quest.registerMission(events);
	}

	/**
	 * Returns a {@link List} of currently active (tracked) {@link Quest}s. This includes all {@link Quest}s which have not
	 * expired (that is, they have been accepted, and they have been completed or not yet expired).
	 * @return The {@link List} of active {@link Quest}s.
	 */
	public List<Quest> getActiveQuests() {
		return activeQuests;
	}

	/**
	 * Adds a {@link Quest} to the {@link List} of selectable {@link Quest}s. Selectable {@link Quest}s can be accepted
	 * by the player through the in-game quest NPC.
	 * @param quest The {@link Quest} to add (this {@link Quest} should not have already been registered).
	 */
	public void addQuest(Quest quest) {
		selectableQuests.add(quest);
		events.trigger(MissionEvent.NEW_QUEST.name());
	}

	/**
	 * Returns a {@link List} of selectable {@link Quest}s. These {@link Quest}s can be accepted in-game through
	 * interaction with the quest NPC.
	 * @return The list of selectable {@link Quest}s.
	 */
	public List<Quest> getSelectableQuests() {
		return selectableQuests;
	}

	/**
	 * Returns all in-game {@link Achievement}s.
	 * @return All in-game {@link Achievement}s.
	 */
	public Achievement[] getAchievements() {
		return achievements;
	}

	/**
	 * Returns the {@link MissionManager}'s {@link EventHandler}, which is responsible for triggering events which
	 * update the state of {@link Mission}s
	 * @return The {@link EventHandler} of the {@link MissionManager}, from which events can be triggered to update the
	 * 		   state of relevant {@link Mission}s.
	 */
	public EventHandler getEvents() {
		return events;
	}

	/**
	 * Updates all active {@link Quest}s' durations through their {@link Quest#updateExpiry()} method.
	 */
	private void updateActiveQuestTimes() {
		for (Quest quest : activeQuests) {
			quest.updateExpiry();
			if (quest.isExpired()) {
				events.trigger(MissionEvent.QUEST_EXPIRED.name());
			}
		}
	}

	@Override
	public void write(Json json) {
		json.writeObjectStart("ActiveQuests");
		for (Quest q : activeQuests) {
			q.write(json);
		}
		json.writeObjectEnd();
		json.writeObjectStart("SelectableQuests");
		for (Quest q : selectableQuests) {
			q.write(json);
		}
		json.writeObjectEnd();
		json.writeObjectStart("Achievements");
		int i = 0;
		for (Achievement achievement : achievements) {
			achievement.write(json, i);
			i++;
		}
		json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonMap) {
		JsonValue active = jsonMap.get("ActiveQuests");
		activeQuests.clear();
		if (active.has("Quest")) {
			active.forEach(jsonValue -> {
				Quest q = FactoryService.getQuests().get(jsonValue.getString("name")).get();
				q.setTimeToExpiry(jsonValue.getInt("expiry"));
				q.setProgress(jsonValue.get("progress"));
				q.getReward().setCollected(jsonValue.getBoolean("collected"));
				activeQuests.add(q);
			});
		}
		JsonValue selectable = jsonMap.get("SelectableQuests");
		selectableQuests.clear();
		if (selectable.has("Quest")) {
			selectable.forEach(jsonValue -> {
				Quest q = FactoryService.getQuests().get(jsonValue.getString("name")).get();
				q.setTimeToExpiry(jsonValue.getInt("expiry"));
				q.setProgress(jsonValue.get("progress"));
				q.getReward().setCollected(jsonValue.getBoolean("collected"));
				selectableQuests.add(q);
			});
		}
		if (selectable.has("Achievement")) {
			selectable.forEach(jsonValue -> {
				Achievement a = achievements[jsonValue.getInt("index")];
				a.setProgress(jsonValue.get("progress"));
			});
		}
	}

	public void setActiveQuests(List<Quest> activeQuests) {
		this.activeQuests.clear();
		this.activeQuests.addAll(activeQuests);
	}

	public void setSelectableQuests(List<Quest> selectableQuests) {
		this.selectableQuests.clear();
		this.selectableQuests.addAll(selectableQuests);
	}
}
