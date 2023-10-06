package com.csse3200.game.missions.rewards;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestRewardTest {

    private QuestReward questReward1, questReward2, questReward3, questReward4;

    private List<Quest> selectableQuests, activeQuests, emptySelectableQuests, emptyActiveQuests;

    @BeforeEach
    public void init() {
        emptyActiveQuests = new ArrayList<>();
        emptySelectableQuests = new ArrayList<>();
        activeQuests = List.of(
                mock(Quest.class),
                mock(Quest.class),
                mock(Quest.class)
        );
        selectableQuests = List.of(
                mock(Quest.class),
                mock(Quest.class),
                mock(Quest.class)
        );

        questReward1 = new QuestReward(emptySelectableQuests, emptyActiveQuests);
        questReward2 = new QuestReward(selectableQuests, emptyActiveQuests);
        questReward3 = new QuestReward(emptySelectableQuests, activeQuests);
        questReward4 = new QuestReward(selectableQuests, activeQuests);

        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
    }

    @AfterEach
    void afterTest() {
        ServiceLocator.clear();
    }

    @Test
    void collectEmptyQuests() {
        MissionManager missionManager = ServiceLocator.getMissionManager();
        List<Quest> oldActive = missionManager.getActiveQuests();
        List<Quest> oldSelectable = missionManager.getSelectableQuests();
        questReward1.collect();
        assertTrue(questReward1.isCollected());
        assertEquals(oldActive, missionManager.getActiveQuests());
        assertEquals(oldSelectable, missionManager.getSelectableQuests());
    }

    @Test
    void collectEmptyActiveQuests() {
        MissionManager missionManager = ServiceLocator.getMissionManager();
        List<Quest> oldActive = missionManager.getActiveQuests();
        List<Quest> oldSelectable = missionManager.getSelectableQuests();
        questReward2.collect();
        assertTrue(questReward2.isCollected());

        for (Quest quest : selectableQuests) {
            oldSelectable.add(quest);
        }

        Assertions.assertEquals(oldActive, missionManager.getActiveQuests());
        Assertions.assertEquals(oldSelectable, missionManager.getSelectableQuests());
    }

    @Test
    void collectEmptySelectableQuests() {
        MissionManager missionManager = ServiceLocator.getMissionManager();
        List<Quest> oldActive = missionManager.getActiveQuests();
        List<Quest> oldSelectable = missionManager.getSelectableQuests();
        questReward3.collect();
        assertTrue(questReward3.isCollected());

        for (Quest quest : activeQuests) {
            oldActive.add(quest);
        }

        Assertions.assertEquals(oldActive, missionManager.getActiveQuests());
        Assertions.assertEquals(oldSelectable, missionManager.getSelectableQuests());
    }

    @Test
    void collectQuestReward() {
        MissionManager missionManager = ServiceLocator.getMissionManager();
        List<Quest> oldActive = missionManager.getActiveQuests();
        List<Quest> oldSelectable = missionManager.getSelectableQuests();
        questReward4.collect();
        assertTrue(questReward4.isCollected());

        for (Quest quest : selectableQuests) {
            oldSelectable.add(quest);
        }

        for (Quest quest : activeQuests) {
            oldActive.add(quest);
        }

        Assertions.assertEquals(oldActive, missionManager.getActiveQuests());
        Assertions.assertEquals(oldSelectable, missionManager.getSelectableQuests());
    }
}
