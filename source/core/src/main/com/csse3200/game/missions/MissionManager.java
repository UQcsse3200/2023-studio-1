package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MissionManager {

	private static final Achievement[] achievements = new Achievement[]{};
	private static final ArrayList<Quest>  quests = new ArrayList<>();
	private static final EventHandler events = new EventHandler();

	public MissionManager() {
		for (Achievement mission: achievements) {
			mission.registerMission(events);
		}
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateHour);
	}

	public void addQuest(Quest quest) {
		quests.add(quest);
		quest.registerMission(events);


	}

	private void updateHour() {
		for (Quest quest: quests) {
			quest.updateExpiry(1);
		}

	}

}
