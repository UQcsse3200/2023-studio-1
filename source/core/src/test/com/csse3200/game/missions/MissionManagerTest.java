package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.achievements.Achievement;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissionManagerTest {

    private Reward r1, r2, r3;
    private Quest q1, q2, q3;
    private Achievement a1, a2, a3;

    @BeforeAll
    public static void begin() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
    }

    @BeforeEach
    public void preTest() {
        r1 = new Reward() {
            public int magicNumber = 0;
            @Override
            public void collect() {
                magicNumber = 1;
            }
        };
        r2 = new Reward() {
            public int magicNumber = 0;
            @Override
            public void collect() {
                magicNumber = 1;
            }
        };
        r3 = new Reward() {
            public int magicNumber = 0;
            @Override
            public void collect() {
                magicNumber = 1;
            }
        };

        q1 = new Quest("My Quest 1", r1, 0, false) {
            private int count = 0;

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("event", () -> { count++; });
            }

            @Override
            public boolean isCompleted() {
                return count == 5;
            }

            @Override
            public String getDescription() {
                return "Long Description 1";
            }

            @Override
            public String getShortDescription() {
                return "Short Description 1";
            }

            @Override
            protected void resetState() {
                count = 0;
            }
        };
        q2 = new Quest("My Quest 2", r2, 1, false) {
            private int count = 5;

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("event", () -> { count--; });
            }

            @Override
            public boolean isCompleted() {
                return count == 0;
            }

            @Override
            public String getDescription() {
                return "Long Description 2";
            }

            @Override
            public String getShortDescription() {
                return "Short Description 2";
            }

            @Override
            protected void resetState() {
                count = 5;
            }
        };
        q3 = new Quest("My Quest 3", r3, 5, true) {
            private int count1 = 0;
            private int count2 = 1;

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("event", () -> { count1++; });
                missionManagerEvents.addListener("otherEvent", () -> { count2 *= 2; });
            }

            @Override
            public boolean isCompleted() {
                return count1 >= 1 && count2 == 16;
            }

            @Override
            public String getDescription() {
                return count1 + " & " + count2;
            }

            @Override
            public String getShortDescription() {
                return "Short Description 3";
            }

            @Override
            protected void resetState() {
                count1 = 0;
                count2 = 1;
            }
        };

        a1 = new Achievement("My Achievement 1") {
            private int count = 0;

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("otherEvent", () -> { count++; });
            }

            @Override
            public boolean isCompleted() {
                return count == 10;
            }

            @Override
            public String getDescription() {
                return "Long Achievement Description 1";
            }

            @Override
            public String getShortDescription() {
                return "Short Achievement Description 1";
            }
        };
        a2 = new Achievement("My Achievement 2") {
            private int count = 10;

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("otherEvent", () -> { count--; });
            }

            @Override
            public boolean isCompleted() {
                return count == 0;
            }

            @Override
            public String getDescription() {
                return "Long Achievement Description 2";
            }

            @Override
            public String getShortDescription() {
                return "Short Achievement Description 2";
            }
        };
        a3 = new Achievement("My Achievement 3") {
            private int count1 = 0;
            private int count2 = 1;

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("event", () -> { count1++; });
                missionManagerEvents.addListener("otherEvent", () -> { count2 *= 2; });
            }

            @Override
            public boolean isCompleted() {
                return count1 == 5 && count2 < 16;
            }

            @Override
            public String getDescription() {
                return count1 + " & " + count2;
            }

            @Override
            public String getShortDescription() {
                return "Short Achievement Description 1";
            }
        };
    }

    @Test
    public void testAcceptNonSelectableQuest() {
        int initialNumberSelectableQuests = ServiceLocator.getMissionManager().getSelectableQuests().size();
        int initialNumberActiveQuests = ServiceLocator.getMissionManager().getActiveQuests().size();

        ServiceLocator.getMissionManager().acceptQuest(q1);
        assertEquals(initialNumberSelectableQuests, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests + 1, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().acceptQuest(q2);
        assertEquals(initialNumberSelectableQuests, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests + 2, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().acceptQuest(q3);
        assertEquals(initialNumberSelectableQuests, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests + 3, ServiceLocator.getMissionManager().getActiveQuests().size());
    }

    @Test
    public void testAddQuest() {
        int initialNumberSelectableQuests = ServiceLocator.getMissionManager().getSelectableQuests().size();
        int initialNumberActiveQuests = ServiceLocator.getMissionManager().getActiveQuests().size();

        ServiceLocator.getMissionManager().addQuest(q1);
        assertEquals(initialNumberSelectableQuests + 1, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().addQuest(q2);
        assertEquals(initialNumberSelectableQuests + 2, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().addQuest(q3);
        assertEquals(initialNumberSelectableQuests + 3, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests, ServiceLocator.getMissionManager().getActiveQuests().size());
    }

    @Test
    public void testAcceptSelectableQuest() {
        int initialNumberSelectableQuests = ServiceLocator.getMissionManager().getSelectableQuests().size();
        int initialNumberActiveQuests = ServiceLocator.getMissionManager().getActiveQuests().size();

        ServiceLocator.getMissionManager().addQuest(q1);
        assertEquals(initialNumberSelectableQuests + 1, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().addQuest(q2);
        assertEquals(initialNumberSelectableQuests + 2, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().addQuest(q3);
        assertEquals(initialNumberSelectableQuests + 3, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().acceptQuest(q1);
        assertEquals(initialNumberSelectableQuests + 2, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests + 1, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().acceptQuest(q2);
        assertEquals(initialNumberSelectableQuests + 1, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests + 2, ServiceLocator.getMissionManager().getActiveQuests().size());

        ServiceLocator.getMissionManager().acceptQuest(q3);
        assertEquals(initialNumberSelectableQuests, ServiceLocator.getMissionManager().getSelectableQuests().size());
        assertEquals(initialNumberActiveQuests + 3, ServiceLocator.getMissionManager().getActiveQuests().size());
    }

}