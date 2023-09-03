package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MissionManager {

	private static final ArrayList<Quest>  quests = new ArrayList<>();
	private static final ArrayList<Achievement> achievements = new ArrayList<>();
	private static final EventHandler events = new EventHandler();

	public MissionManager() {
		for (Mission mission: quests) {
			mission.registerMission();
		}
		for (Mission mission: achievements) {
			mission.registerMission();
		}
	}

}
