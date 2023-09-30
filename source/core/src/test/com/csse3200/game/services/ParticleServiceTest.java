package com.csse3200.game.services;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.ParticleEffectWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticleServiceTest {

    private ParticleService.ParticleEffectType particleEffectType1, particleEffectType2, particleEffectType3;
    private ParticleEffectWrapper particleEffectWrapper;
    private ParticleEffect particleEffect;
    private ArrayList<ParticleEffectWrapper> queuedEffects;
    private HashMap<ParticleService.ParticleEffectType, ParticleEffectPool> particleEffectPools;
    private ParticleService particleService;
    private ParticleEffectPool.PooledEffect pooledEffect;

    private ParticleEffectPool particleEffectPool;

    @BeforeEach
    public void setUp()  {
        particleEffectType1 = ParticleService.ParticleEffectType.ACID_RAIN;
        particleEffectType2 = ParticleService.ParticleEffectType.ACID_RAIN;
        particleEffectType3 = ParticleService.ParticleEffectType.ACID_RAIN;
        particleEffectWrapper = new ParticleEffectWrapper(pooledEffect, ParticleService.WEATHER_EVENT, "ACID_RAIN");
        particleEffect = new ParticleEffect();
        particleEffectPool = new ParticleEffectPool(particleEffect, 5, 10);
        particleService = mock(ParticleService.class);
        ServiceLocator.registerParticleService(particleService);
        particleEffectPools = new HashMap<>();
        particleEffectPools.put(particleEffectType1, particleEffectPool);
        queuedEffects = new ArrayList<>();
    }

    @Test
    public void testParticleEffectType() {
        assertEquals(particleEffectType1.getCategory(), ParticleService.WEATHER_EVENT);
        assertEquals(particleEffectType2.getCategory(), ParticleService.WEATHER_EVENT);
        assertEquals(particleEffectType3.getCategory(), ParticleService.WEATHER_EVENT);
    }

    @Test
    public void testStartEffect() {
        particleService.startEffect(particleEffectType1);
        ParticleEffect mockParticleEffect = mock(ParticleEffect.class);
        ParticleEffectWrapper mockParticleEffectWrapper = mock(ParticleEffectWrapper.class);
        queuedEffects.add(mockParticleEffectWrapper);
        assertTrue(queuedEffects.contains(mockParticleEffectWrapper));
        verify(particleService).startEffect(particleEffectType1);
    }
}
