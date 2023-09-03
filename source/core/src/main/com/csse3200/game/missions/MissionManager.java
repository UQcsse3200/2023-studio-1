package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MissionManager {

	private static final Achievement[] achievements = new Achievement[]{};
	private static final ArrayList<Quest> quests = new ArrayList<>();
	private static final EventHandler events = new EventHandler();

	/**
	 * Creates the mission manager, registered all game achievements and adds a listener for hourly updates
	 */
	public MissionManager() {
		for (Achievement mission : achievements) {
			mission.registerMission(events);
		}
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateHour);
	}

	/**
	 * Adds a quest to the list of active quests in the game.  Also registers this quest in the game
	 * @param quest quest to be added and registered
	 */
	public void addQuest(Quest quest) {
		quests.add(quest);
		quest.registerMission(events);


	}

	/**
	 * Method that is called every in-game hour which loops through the active quests and updates their expiry time
	 */
	private void updateHour() {
		for (Quest quest : quests) {
			quest.updateExpiry(1);
		}

	}

}
