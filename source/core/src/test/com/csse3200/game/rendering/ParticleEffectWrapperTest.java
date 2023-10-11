package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.csse3200.game.services.ParticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

class ParticleEffectWrapperTest {

    private ParticleEffectPool.PooledEffect mockPooledEffect1, mockPooledEffect2, mockPooledEffect3;
    private String category;
    private String type;
    private ParticleEffectWrapper particleEffectWrapper1, particleEffectWrapper2, particleEffectWrapper3;

    @BeforeEach
    void setUp() {
        mockPooledEffect1 = mock(ParticleEffectPool.PooledEffect.class);
        mockPooledEffect2 = mock(ParticleEffectPool.PooledEffect.class);
        mockPooledEffect3 = mock(ParticleEffectPool.PooledEffect.class);
        category = ParticleService.WEATHER_EVENT;
        type = ParticleService.ParticleEffectType.ACID_RAIN.name();
        particleEffectWrapper1 = new ParticleEffectWrapper(mockPooledEffect1, category, type);
        particleEffectWrapper2 = new ParticleEffectWrapper(mockPooledEffect2, category, type);
        particleEffectWrapper3 = new ParticleEffectWrapper(mockPooledEffect3, category, type);
    }

    @Test
    void testConstructor() {
        assertEquals(mockPooledEffect1, particleEffectWrapper1.getPooledEffect());
        assertEquals(mockPooledEffect2, particleEffectWrapper2.getPooledEffect());
        assertEquals(mockPooledEffect3, particleEffectWrapper3.getPooledEffect());
        assertEquals(category, particleEffectWrapper1.getCategory());
        assertEquals(type, particleEffectWrapper1.getType());
        assertEquals(category, particleEffectWrapper2.getCategory());
        assertEquals(type, particleEffectWrapper2.getType());
        assertEquals(category, particleEffectWrapper3.getCategory());
        assertEquals(type, particleEffectWrapper3.getType());
    }

    @Test
    void testGetPooledEffect() {
        assertEquals(mockPooledEffect1, particleEffectWrapper1.getPooledEffect());
        assertEquals(mockPooledEffect2, particleEffectWrapper2.getPooledEffect());
        assertEquals(mockPooledEffect3, particleEffectWrapper3.getPooledEffect());
        assertNotEquals(mockPooledEffect1, particleEffectWrapper2.getPooledEffect());
        assertNotEquals(mockPooledEffect2, particleEffectWrapper3.getPooledEffect());
        assertNotEquals(mockPooledEffect3, particleEffectWrapper1.getPooledEffect());
    }

    @Test
    void testGetCategory() {
        assertEquals(category, particleEffectWrapper1.getCategory());
        assertEquals(category, particleEffectWrapper2.getCategory());
        assertEquals(category, particleEffectWrapper3.getCategory());
    }

    @Test
    void testGetType() {
        assertEquals(type, particleEffectWrapper1.getType());
        assertEquals(type, particleEffectWrapper2.getType());
        assertEquals(type, particleEffectWrapper3.getType());
    }
}
