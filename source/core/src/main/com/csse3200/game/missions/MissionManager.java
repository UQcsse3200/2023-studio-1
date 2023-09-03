package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MissionManager {

	private static final Achievement[] achievements = new Achievement[]{};
	private static final ArrayList<Quest>  quests = new ArrayList<>();
	private static final EventHandler events = new EventHandler();

	public MissionManager() {
		for (Mission mission: achievements) {
			mission.registerMission(events);
		}
	}

	public void addQuest(Quest quest) {

	}

	private void updateHour() {

	}

}
