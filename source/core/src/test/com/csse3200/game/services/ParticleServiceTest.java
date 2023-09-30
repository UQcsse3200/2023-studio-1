package com.csse3200.game.services;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticleServiceTest {

    ParticleService particleService;

    @BeforeEach
    public void setUp() {
        ServiceLocator.clear();
        particleService = new ParticleService();
        ServiceLocator.registerParticleService(particleService);
    }

    @AfterEach
    public void cleanUp() {
        ServiceLocator.clear();
    }
}
