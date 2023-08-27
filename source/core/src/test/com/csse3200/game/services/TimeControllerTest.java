package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class TimeControllerTest {
    
    static GameTime mockGameTime;
    TimeController timeController;
    GameTimeDisplay mockGameTimeDisplay;
    
    @BeforeAll
    static void beforeAll() {
        mockGameTime = mock(GameTime.class);
        when(mockGameTime.getActiveTime()).thenReturn(300000L);
        when(mockGameTime.getTime()).thenReturn(40000L);
        when(mockGameTime.getTimeSince(40000L)).thenReturn(20000L);
    }
    
    @BeforeEach
    void setUp() {
        timeController = new TimeController();
        mockGameTimeDisplay = mock(GameTimeDisplay.class);
        timeController.setTimeSource(mockGameTime);
        timeController.setTimeDisplay(mockGameTimeDisplay);
    }
    /*
    @AfterEach
    void tearDown() {
    }
    
    @Test
    void setTimeSource() {
    }
    
    @Test
    void setTimeDisplay() {
    }
    */
    @Test
    void getTimeInSeconds() {
        assertEquals((int) 300, timeController.getTimeInSeconds());
    }
    
    @Test
    void getTimeOfDay() {
        assertEquals((int) 300000, timeController.getTimeOfDay());
    }
    
    @Test
    void getHour() {
        assertEquals((int) 10, timeController.getHour());
    }
    
    @Test
    void updateDisplay() {
        timeController.updateDisplay();
        verify(mockGameTimeDisplay, times(1)).update(10);
    }
    /*
    @Test
    void pause() {
        timeController.pause();
        verify(mockGameTime, times(1)).getTime();
    }
    */
    @Test
    void pauseUnpause() {
        timeController.pause();
        verify(mockGameTime, times(1)).getTime();
        
        timeController.unpause();
        verify(mockGameTime, times(1)).getTimeSince(40000L);
        verify(mockGameTime, times(1)).addPauseOffset(20000L);
    }
    
    @Test
    void setTime() {
        timeController.setTime(7);
        verify(mockGameTimeDisplay, times(1)).update(7);
    }
}