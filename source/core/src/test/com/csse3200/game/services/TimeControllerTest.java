package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
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
        timeController = new TimeController(mockGameTime);
        mockGameTimeDisplay = mock(GameTimeDisplay.class);
        timeController.registerTimeDisplay(mockGameTimeDisplay);
    }
    
    @Test
    void getTimeInSeconds() {
        assertEquals(300, timeController.getTimeInSeconds(),
                "300 seconds not given");
    }
    
    @Test
    void getTimeOfDay() {
        assertEquals(300000, timeController.getTimeOfDay(),
        "Incorrect time of day returned; 300000 not given.");
    }
    
    @Test
    void getHour() {
        assertEquals(10, timeController.getHour(),
                "Hour 10 not given.");
    }
    
    @Test
    void updateDisplay() {
        timeController.updateDisplay();
        verify(mockGameTimeDisplay, times(1)).update(10);
    }

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
        verify(mockGameTime, atLeastOnce()).getActiveTime();
    }
}