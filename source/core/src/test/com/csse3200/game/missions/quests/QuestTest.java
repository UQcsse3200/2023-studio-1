package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestTest {

    private Reward r1, r2, r3, r4, r5;
    private Quest q1, q2, q3, q4, q5;

    @BeforeEach
    void preTest() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());

        r1 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r2 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r3 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r4 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r5 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };

        q1 = new Quest("My Quest 1", r1, 0, false) {
            private int count = 0;

            @Override
            protected void resetState() {
                count = 0;
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("e1", () -> { count++; notifyUpdate(); });
            }

            @Override
            public boolean isCompleted() {
                return count == 0;
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
            public void readProgress(JsonValue progress) {
            }

            @Override
            public JsonValue getProgress() {
                return null;
            }
        };
        q2 = new Quest("My Quest 2", r2, 1, false) {
            private int count = 0;

            @Override
            protected void resetState() {
                count = 0;
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("e1", () -> { count++; notifyUpdate(); });
            }

            @Override
            public boolean isCompleted() {
                return count % 2 == 1;
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
            public void readProgress(JsonValue progress) {
            }

            @Override
            public JsonValue getProgress() {
                return null;
            }
        };
        q3 = new Quest("My Quest 3", r3, 1, true) {
            private int count = 4;

            @Override
            protected void resetState() {
                count = 4;
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("e1", () -> { count--; notifyUpdate(); });
                missionManagerEvents.addListener("e2", () -> { count++; });
            }

            @Override
            public boolean isCompleted() {
                return count == 0;
            }

            @Override
            public String getDescription() {
                return "Long Description 3";
            }

            @Override
            public String getShortDescription() {
                return "Short Description 3";
            }

            @Override
            public void readProgress(JsonValue progress) {
            }

            @Override
            public JsonValue getProgress() {
                return null;
            }
        };
        q4 = new Quest("My Quest 4", r4, 4, false) {
            private int count = 0;
            private boolean isTrue = false;

            @Override
            protected void resetState() {
                count = 0;
                isTrue = false;
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("e1", () -> { isTrue = true; notifyUpdate(); });
                missionManagerEvents.addListener("e2", () -> { count++; isTrue = !isTrue; notifyUpdate(); });
            }

            @Override
            public boolean isCompleted() {
                return isTrue && count >= 2 && count <= 4;
            }

            @Override
            public String getDescription() {
                return isTrue + " & " + count;
            }

            @Override
            public String getShortDescription() {
                return "" + count;
            }

            @Override
            public void readProgress(JsonValue progress) {
            }

            @Override
            public JsonValue getProgress() {
                return null;
            }
        };
        q5 = new Quest("My Quest 5", r5, 4, true) {
            private int count = 10;

            @Override
            protected void resetState() {
                count = 10;
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("e2", () -> {
                    if (count % 2 == 1) {
                        count = 3 * count + 1;
                    }
                    count /= 2;
                    notifyUpdate();
                });
            }

            @Override
            public boolean isCompleted() {
                return count == 1;
            }

            @Override
            public String getDescription() {
                return "Collatz Conjecture";
            }

            @Override
            public String getShortDescription() {
                return "At: " + count;
            }

            @Override
            public void readProgress(JsonValue progress) {
            }

            @Override
            public JsonValue getProgress() {
                return null;
            }
        };
    }

    @AfterEach
    void postTest() {
        ServiceLocator.clear();
    }

    @Test
    void testGetName() {
        assertEquals("My Quest 1", q1.getName());
        assertEquals("My Quest 2", q2.getName());
        assertEquals("My Quest 3", q3.getName());
        assertEquals("My Quest 4", q4.getName());
        assertEquals("My Quest 5", q5.getName());
    }

    @Test
    void testIsMandatory() {
        assertFalse(q1.isMandatory());
        assertFalse(q2.isMandatory());
        assertTrue(q3.isMandatory());
        assertFalse(q4.isMandatory());
        assertTrue(q5.isMandatory());
    }

    @Test
    void testGetDescription() {
        assertEquals("Long Description 1", q1.getDescription());
        assertEquals("Long Description 2", q2.getDescription());
        assertEquals("Long Description 3", q3.getDescription());
        assertEquals("false & 0", q4.getDescription());
        assertEquals("Collatz Conjecture", q5.getDescription());

        q4.registerMission(ServiceLocator.getMissionManager().getEvents());

        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        assertEquals("true & 0", q4.getDescription());
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals("false & 1", q4.getDescription());
    }

    @Test
    void testGetShortDescription() {
        assertEquals("Short Description 1", q1.getShortDescription());
        assertEquals("Short Description 2", q2.getShortDescription());
        assertEquals("Short Description 3", q3.getShortDescription());
        assertEquals("0", q4.getShortDescription());
        assertEquals("At: 10", q5.getShortDescription());

        q4.registerMission(ServiceLocator.getMissionManager().getEvents());
        q5.registerMission(ServiceLocator.getMissionManager().getEvents());

        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals("1", q4.getShortDescription());
        assertEquals("At: 5", q5.getShortDescription());
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals("2", q4.getShortDescription());
        assertEquals("At: 8", q5.getShortDescription());
    }

    @Test
    void testQuestsExpiry() {
        Quest nonExpireQuest = new AutoQuest("Quest 6", r1, "Description");
        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());
        assertFalse(q4.isExpired());
        assertFalse(q5.isExpired());

        assertFalse(nonExpireQuest.isExpired());

        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        nonExpireQuest.updateExpiry();

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
        assertFalse(q4.isExpired());
        assertFalse(q5.isExpired());

        assertFalse(nonExpireQuest.isExpired());

        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        nonExpireQuest.updateExpiry();

        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        nonExpireQuest.updateExpiry();

        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        nonExpireQuest.updateExpiry();

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
        assertTrue(q4.isExpired());
        assertTrue(q5.isExpired());

        assertFalse(nonExpireQuest.isExpired());

        nonExpireQuest.resetExpiry();
        
        assertFalse(nonExpireQuest.isExpired());
    }

    @Test
    void testIsCompleted() {
        assertTrue(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());
        assertFalse(q4.isCompleted());
        assertFalse(q5.isCompleted());

        q1.registerMission(ServiceLocator.getMissionManager().getEvents());
        q2.registerMission(ServiceLocator.getMissionManager().getEvents());
        q3.registerMission(ServiceLocator.getMissionManager().getEvents());
        q4.registerMission(ServiceLocator.getMissionManager().getEvents());
        q5.registerMission(ServiceLocator.getMissionManager().getEvents());

        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        assertFalse(q1.isCompleted());
        assertTrue(q2.isCompleted());
        assertFalse(q3.isCompleted());
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        assertFalse(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertTrue(q3.isCompleted());

        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertFalse(q3.isCompleted());
        assertTrue(q4.isCompleted());
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertTrue(q5.isCompleted());
    }

    @Test
    void testCollectRewards() {
        assertFalse(r1.isCollected());
        q1.collectReward();
        assertTrue(r1.isCollected());

        assertFalse(r2.isCollected());
        q2.collectReward();
        assertFalse(r2.isCollected());
        q2.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        q2.collectReward();
        assertTrue(r2.isCollected());

        assertFalse(r3.isCollected());
        q3.collectReward();
        assertFalse(r3.isCollected());
        q3.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        q3.collectReward();
        assertTrue(r3.isCollected());

        assertFalse(r4.isCollected());
        q4.collectReward();
        assertFalse(r4.isCollected());
        q4.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        q4.collectReward();
        assertTrue(r4.isCollected());

        assertFalse(r5.isCollected());
        q5.collectReward();
        assertFalse(r5.isCollected());
        q5.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        q5.collectReward();
        assertTrue(r5.isCollected());
    }

    @Test
    void testCollectRewardOnlyCollectsOnce() {
        int[] counts = new int[]{0};
        Reward r = new Reward() {
            @Override
            public void collect() {
                counts[0]++;
                setCollected();
            }
        };
        Quest q = new Quest("", r, 0, false) {
            @Override
            protected void resetState() {
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
            }

            @Override
            public boolean isCompleted() {
                return true;
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
            public void readProgress(JsonValue progress) {
            }

            @Override
            public JsonValue getProgress() {
                return null;
            }
        };

        assertEquals(0, counts[0]);
        q.collectReward();
        assertEquals(1, counts[0]);
        q.collectReward();
        assertEquals(1, counts[0]);
        q.collectReward();
        assertEquals(1, counts[0]);
    }

    @Test
    void testResetExpiryResetsTimeToExpiry() {
        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());
        assertFalse(q4.isExpired());
        assertFalse(q5.isExpired());

        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        q4.updateExpiry();
        q5.updateExpiry();

        q4.updateExpiry();
        q5.updateExpiry();

        q4.updateExpiry();
        q5.updateExpiry();

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
        assertTrue(q4.isExpired());
        assertTrue(q5.isExpired());

        q1.resetExpiry();
        q2.resetExpiry();
        q3.resetExpiry();
        q4.resetExpiry();
        q5.resetExpiry();

        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());
        assertFalse(q4.isExpired());
        assertFalse(q5.isExpired());

        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        q4.updateExpiry();
        q5.updateExpiry();

        q4.updateExpiry();
        q5.updateExpiry();

        q4.updateExpiry();
        q5.updateExpiry();

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
        assertTrue(q4.isExpired());
        assertTrue(q5.isExpired());
    }

    @Test
    void testResetExpiryResetsState() {
        assertTrue(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());
        assertFalse(q4.isCompleted());
        assertFalse(q5.isCompleted());

        q5.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        q3.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        q4.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        q2.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getMissionManager().getEvents().trigger("e1");

        assertTrue(q1.isCompleted());
        assertTrue(q2.isCompleted());
        assertTrue(q3.isCompleted());
        assertTrue(q4.isCompleted());
        assertTrue(q5.isCompleted());

        q1.resetExpiry();
        q2.resetExpiry();
        q3.resetExpiry();
        q4.resetExpiry();
        q5.resetExpiry();

        assertTrue(q1.isCompleted());
        assertFalse(q2.isCompleted());
        assertFalse(q3.isCompleted());
        assertFalse(q4.isCompleted());
        assertFalse(q5.isCompleted());
    }

    @Test
    void testQuestCompletionTriggersEvent() {
        int[] counts = new int[]{0};
        ServiceLocator.getMissionManager().getEvents().addListener(
                MissionManager.MissionEvent.MISSION_COMPLETE.name(),
                (String ignored) -> { counts[0]++; }
        );

        q1.registerMission(ServiceLocator.getMissionManager().getEvents());
        q2.registerMission(ServiceLocator.getMissionManager().getEvents());
        q3.registerMission(ServiceLocator.getMissionManager().getEvents());
        q4.registerMission(ServiceLocator.getMissionManager().getEvents());
        q5.registerMission(ServiceLocator.getMissionManager().getEvents());

        assertEquals(0, counts[0]);

        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        assertEquals(1, counts[0]);
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        assertEquals(1, counts[0]);
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        assertEquals(2, counts[0]);
        ServiceLocator.getMissionManager().getEvents().trigger("e1");
        assertEquals(3, counts[0]);

        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals(3, counts[0]);
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals(4, counts[0]);
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals(4, counts[0]);
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals(5, counts[0]);
        ServiceLocator.getMissionManager().getEvents().trigger("e2");
        assertEquals(6, counts[0]);
    }
}