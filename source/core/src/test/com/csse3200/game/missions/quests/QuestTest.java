package com.csse3200.game.missions.quests;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestTest {

    private Reward r1, r2, r3, r4, r5;
    private Quest q1, q2, q3, q4, q5;

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
        r4 = new Reward() {
            @Override
            public void collect() {
            }
        };
        r5 = new Reward() {
            @Override
            public void collect() {
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
                missionManagerEvents.addListener("e1", () -> { count++; });
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
        };
        q2 = new Quest("My Quest 2", r2, 1, false) {
            private int count = 0;

            @Override
            protected void resetState() {
                count = 0;
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("e1", () -> { count++; });
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
        };
        q3 = new Quest("My Quest 3", r3, 1, true) {
            private int count = 4;

            @Override
            protected void resetState() {
                count = 4;
            }

            @Override
            public void registerMission(EventHandler missionManagerEvents) {
                missionManagerEvents.addListener("e1", () -> { count--; });
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
                missionManagerEvents.addListener("e1", () -> { isTrue = true; });
                missionManagerEvents.addListener("e2", () -> { count++; isTrue = !isTrue; });
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
        };
    }

    @Test
    public void testGetName() {
        assertEquals("My Quest 1", q1.getName());
        assertEquals("My Quest 2", q2.getName());
        assertEquals("My Quest 3", q3.getName());
        assertEquals("My Quest 4", q4.getName());
        assertEquals("My Quest 5", q5.getName());
    }

    @Test
    public void testIsMandatory() {
        assertFalse(q1.isMandatory());
        assertFalse(q2.isMandatory());
        assertTrue(q3.isMandatory());
        assertFalse(q4.isMandatory());
        assertTrue(q5.isMandatory());
    }

    @Test
    public void testQuestsExpiry() {
        assertTrue(q1.isExpired());
        assertFalse(q2.isExpired());
        assertFalse(q3.isExpired());
        assertFalse(q4.isExpired());
        assertFalse(q5.isExpired());

        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
        assertFalse(q4.isExpired());
        assertFalse(q5.isExpired());

        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();
        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();
        q1.updateExpiry();
        q2.updateExpiry();
        q3.updateExpiry();
        q4.updateExpiry();
        q5.updateExpiry();

        assertTrue(q1.isExpired());
        assertTrue(q2.isExpired());
        assertTrue(q3.isExpired());
        assertTrue(q4.isExpired());
        assertTrue(q5.isExpired());
    }

}