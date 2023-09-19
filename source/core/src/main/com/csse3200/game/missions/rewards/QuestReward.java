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
    private List<Quest> quests;

    /**
     * A QuestReward that adds quests to the MissionManager on collect().
     * @param quests - the quests to be added when the Quest attached is complete.
     */
    public QuestReward(List<Quest> quests) {
        this.quests = quests;
    }

    /**
     * When called will add the QuestReward's quests to the MissionManager.
     */
    @Override
    public void collect() {
        MissionManager missionManager = ServiceLocator.getMissionManager();

        for (Quest quest : this.quests) {
            missionManager.addQuest(quest);
        }
    }
}
