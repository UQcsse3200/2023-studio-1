package com.csse3200.game.missions.rewards;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestRewardTest {

    private QuestReward questReward1, questReward2, questReward3, questReward4;

    private List<Quest> selectableQuests, activeQuests, emptySelectableQuests, emptyActiveQuests;

    private MissionManager missionManager;

    @BeforeEach
    public void init() {
        missionManager = mock(MissionManager.class);
        when(ServiceLocator.getMissionManager()).thenReturn(missionManager);

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
    }

    @Test
    public void collectEmptyQuests() {
        questReward1.collect();
        assertTrue(questReward1.isCollected());

        verifyNoInteractions(missionManager);
    }

    @Test
    public void collectEmptyActiveQuests() {
        questReward2.collect();
        assertTrue(questReward2.isCollected());

        for (Quest quest : selectableQuests) {
            verify(missionManager).addQuest(quest);
        }
    }

    @Test
    public void collectEmptySelectableQuests() {
        questReward3.collect();
        assertTrue(questReward3.isCollected());

        for (Quest quest : activeQuests) {
            verify(missionManager).addQuest(quest);
        }
    }

    @Test
    public void collectQuestReward() {
        questReward4.collect();
        assertTrue(questReward4.isCollected());

        for (Quest quest : selectableQuests) {
            verify(missionManager).addQuest(quest);
        }

        for (Quest quest : activeQuests) {
            verify(missionManager).addQuest(quest);
        }
    }
}
