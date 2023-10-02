package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.utils.JsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RewardTest {
    JsonValue isTrue, isFalse;
    Reward reward;

    @BeforeEach
    public void init() {
        isTrue = mock(JsonValue.class);
        when(isTrue.getBoolean("collected")).thenReturn(true);
        isFalse = mock(JsonValue.class);
        when(isFalse.getBoolean("collected")).thenReturn(false);

        reward = new Reward() {
            @Override
            public void collect() {
                // do nothing
            }
        };
    }

    @Test
    public void readTrue() {
        reward.read(isTrue);
        assertTrue(reward.isCollected());
    }

    @Test
    public void readFalse() {
        reward.read(isFalse);
        assertFalse(reward.isCollected());
    }

    @Test
    public void testCollected() {
        assertFalse(reward.isCollected());
        reward.setCollected();
        assertTrue(reward.isCollected());
    }
}
