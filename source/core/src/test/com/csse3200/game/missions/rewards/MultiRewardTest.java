package com.csse3200.game.missions.rewards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MultiRewardTest {
    private MultiReward multiReward1, multiReward2;
    private List<Reward> rewards;

    @BeforeEach
    public void init() {
        multiReward1 = new MultiReward(new ArrayList<>());

        rewards = List.of(
                mock(Reward.class),
                mock(Reward.class),
                mock(Reward.class)
        );

        multiReward2 = new MultiReward(rewards);
    }

    @Test
    public void testEmptyCollect() {
        multiReward1.collect();
        assertTrue(multiReward1.isCollected());
    }

    @Test
    public void nonEmptyCollect() {
        multiReward2.collect();
        assertTrue(multiReward2.isCollected());

        for (Reward reward : rewards) {
            verify(reward).collect();
        }
    }
}
