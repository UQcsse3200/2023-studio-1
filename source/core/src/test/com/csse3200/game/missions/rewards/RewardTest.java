package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.utils.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RewardTest {
    JsonValue isTrue, isFalse;
    Reward reward;

    @BeforeEach
    void init() {
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
    void readTrue() {
        reward.read(isTrue);
        Assertions.assertTrue(reward.isCollected());
    }

    @Test
    void readFalse() {
        reward.read(isFalse);
        Assertions.assertFalse(reward.isCollected());
    }

    @Test
    void testCollected() {
        Assertions.assertFalse(reward.isCollected());
        reward.setCollected();
        Assertions.assertTrue(reward.isCollected());
    }
}
