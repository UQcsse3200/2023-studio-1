package com.csse3200.game.services;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.ParticleEffectWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParticleServiceTest {

    private ParticleService.ParticleEffectType particleEffectType1, particleEffectType2, particleEffectType3;

    @BeforeEach
    public void setUp()  {
        particleEffectType1 = ParticleService.ParticleEffectType.ACID_RAIN;
        particleEffectType2 = ParticleService.ParticleEffectType.ACID_RAIN;
        particleEffectType3 = ParticleService.ParticleEffectType.ACID_RAIN;
    }

    @Test
    public void testEnumValues() {
        assertEquals(particleEffectType1.getCategory(), ParticleService.WEATHER_EVENT);
        assertEquals(particleEffectType2.getCategory(), ParticleService.WEATHER_EVENT);
        assertEquals(particleEffectType3.getCategory(), ParticleService.WEATHER_EVENT);
    }
}
