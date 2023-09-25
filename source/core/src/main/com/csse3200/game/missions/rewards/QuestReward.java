package com.csse3200.game.missions.rewards;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * The QuestReward can be added as a reward to a quest so that when the quest is complete the collect() method
 * will add the quests from the constructor to the MissionManager.
 */
public class QuestReward extends Reward {

    /**
     * A list of quests to be unlocked when this reward is marked as complete.
     */
    private final List<Quest> selectableQuests;
    /**
     * A list of quests to be made active when this reward is collected.
     */
    private final List<Quest> activeQuests;

    /**
     * A QuestReward that adds quests to the MissionManager on collect().
     * @param selectableQuests - the quests to be added when the Quest attached is complete.
     */
    public QuestReward(List<Quest> selectableQuests, List<Quest> activeQuests) {
        super();
        this.selectableQuests = selectableQuests;
        this.activeQuests = activeQuests;
    }

    /**
     * When called will add the QuestReward's quests to the MissionManager.
     */
    @Override
    public void collect() {
        MissionManager missionManager = ServiceLocator.getMissionManager();

        for (Quest quest : selectableQuests) {
            missionManager.addQuest(quest);
        }

        for (Quest quest : activeQuests) {
            missionManager.acceptQuest(quest);
        }
    }
}
