package com.csse3200.game.missions;

import com.csse3200.game.events.EventHandler;
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

    @BeforeAll
    public static void begin() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
    }

    @BeforeEach
    public void preTest() {
        r1 = new Reward() {
            @Override
            public void collect() {
            }
        };
        r2 = new Reward() {
            @Override
            public void collect() {
            }
        };
        r3 = new Reward() {
            @Override
            public void collect() {
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
                return null;
            }

            @Override
            public String getShortDescription() {
                return null;
            }

            @Override
            protected void resetState() {
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
                return null;
            }

            @Override
            public String getShortDescription() {
                return null;
            }

            @Override
            protected void resetState() {
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
                return null;
            }

            @Override
            public String getShortDescription() {
                return null;
            }

            @Override
            protected void resetState() {
            }
        };
    }

    @Test
    public void testAchievementsLoadCorrectly() {
        assertEquals("Plant President", ServiceLocator.getMissionManager().getAchievements()[0].getName());
        assertEquals("Crop Enjoyer", ServiceLocator.getMissionManager().getAchievements()[1].getName());
        assertEquals("Gardener of the Galaxy", ServiceLocator.getMissionManager().getAchievements()[2].getName());
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

    @Test
    public void testSelectableQuestsDoNotUpdate() {
        ServiceLocator.getMissionManager().addQuest(q1);
        ServiceLocator.getMissionManager().addQuest(q2);
        ServiceLocator.getMissionManager().addQuest(q3);

        assertFalse(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());

        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");

        assertFalse(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());
    }

    @Test
    public void testActiveQuestsUpdate() {
        ServiceLocator.getMissionManager().acceptQuest(q1);
        ServiceLocator.getMissionManager().acceptQuest(q2);
        ServiceLocator.getMissionManager().acceptQuest(q3);

        assertFalse(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());

        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");

        assertTrue(q1.isCompleted());
        assertTrue(q2.isCompleted());
        assertTrue(q3.isCompleted());
    }

    @Test
    public void testQuestsOnlyUpdateOnceAccepted() {
        ServiceLocator.getMissionManager().addQuest(q1);
        ServiceLocator.getMissionManager().addQuest(q2);
        ServiceLocator.getMissionManager().addQuest(q3);

        assertFalse(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());

        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");

        assertFalse(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());

        ServiceLocator.getMissionManager().acceptQuest(q1);
        ServiceLocator.getMissionManager().acceptQuest(q2);
        ServiceLocator.getMissionManager().acceptQuest(q3);

        assertFalse(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());

        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("event");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");
        ServiceLocator.getMissionManager().getEvents().trigger("otherEvent");

        assertTrue(q1.isCompleted());
        assertTrue(q2.isCompleted());
        assertTrue(q3.isCompleted());
    }

    @Test
    public void testSelectableQuestsDoNotExpire() {
        ServiceLocator.getMissionManager().addQuest(q1);
        ServiceLocator.getMissionManager().addQuest(q2);
        ServiceLocator.getMissionManager().addQuest(q3);

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());
    }

    @Test
    public void testActiveQuestsExpire() {
        ServiceLocator.getMissionManager().acceptQuest(q1);
        ServiceLocator.getMissionManager().acceptQuest(q2);
        ServiceLocator.getMissionManager().acceptQuest(q3);

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
    }

    @Test
    public void testQuestsOnlyExpireOnceAccepted() {
        ServiceLocator.getMissionManager().addQuest(q1);
        ServiceLocator.getMissionManager().addQuest(q2);
        ServiceLocator.getMissionManager().addQuest(q3);

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getMissionManager().acceptQuest(q1);
        ServiceLocator.getMissionManager().acceptQuest(q2);
        ServiceLocator.getMissionManager().acceptQuest(q3);

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertFalse(q3.isExpired());

        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");
        ServiceLocator.getTimeService().getEvents().trigger("updateHour");

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
    }

}