package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeControllerTest {
    
    TimeController timeController;
    
    @BeforeAll
    static void beforeAll() {
        GameTime mockGameTime = mock(GameTime.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(10f);
    }
    
    @BeforeEach
    void setUp() {
        timeController = new TimeController();
    }
    
    @AfterEach
    void tearDown() {
    }
    
    @Test
    void setTimeSource() {
    
    }
    
    @Test
    void setTimeDisplay() {
    }
    
    @Test
    void getTimeInSeconds() {
    }
    
    @Test
    void getTimeOfDay() {
    }
    
    @Test
    void getHour() {
    }
    
    @Test
    void updateDisplay() {
    }
    
    @Test
    void pause() {
    }
    
    @Test
    void unpause() {
    }
    
    @Test
    void setTime() {
    }
}