package com.csse3200.game.missions.rewards;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.AutoQuest;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;
import java.util.function.Supplier;

/**
 * The QuestReward can be added as a reward to a quest so that when the quest is complete the collect() method
 * will add the quests from the constructor to the MissionManager.
 */
public class QuestReward extends Reward {

    /**
     * A list of quests to be unlocked when this reward is marked as complete.
     */
    private final List<Supplier<Quest>> selectableQuests;
    /**
     * A list of quests to be made active when this reward is collected.
     */
    private final List<Supplier<Quest>> activeQuests;

    /**
     * A QuestReward that adds quests to the MissionManager on collect().
     * @param selectableQuests - the quests to be added when the Quest attached is complete.
     * @param activeQuests - the quests to be activated when the reward is collected.
     */
    public QuestReward(List<Supplier<Quest>> selectableQuests, List<Supplier<Quest>> activeQuests) {
        super();
        this.selectableQuests = selectableQuests;
        this.activeQuests = activeQuests;
    }

    /**
     * When called will add the QuestReward's quests to the MissionManager.
     */
    @Override
    public void collect() {
        setCollected();
        MissionManager missionManager = ServiceLocator.getMissionManager();

        for (Supplier<Quest> questSupplier : selectableQuests) {
            missionManager.addQuest(questSupplier.get());
        }

        for (Supplier<Quest> questSupplier : activeQuests) {
            Quest quest = questSupplier.get();
            missionManager.acceptQuest(quest);
            if (quest instanceof AutoQuest) {
                if (quest.getName().equals("Connection")) {
                    ServiceLocator.getMissionManager().getEvents().trigger("An Agreement");
                } else if (quest.getName().equals("An Imminent Threat")) {
                    ServiceLocator.getMissionManager().getEvents().trigger("Making Contact");
                }
            }
        }
    }
}
