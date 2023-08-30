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
    
    static GameTime mockGameTimeStandard;
    static GameTime mockGameTimeAt12Mins;
    static GameTime mockGameTimePast12Mins;
    TimeController timeController;
    TimeController timeControllerAt12Mins;
    TimeController timeControllerPast12Mins;
    GameTimeDisplay mockGameTimeDisplay;
    
    @BeforeAll
    static void beforeAll() {
        mockGameTimeStandard = mock(GameTime.class);
        when(mockGameTimeStandard.getActiveTime()).thenReturn(300000L);
        when(mockGameTimeStandard.getTime()).thenReturn(40000L);
        when(mockGameTimeStandard.getTimeSince(40000L)).thenReturn(20000L);
    
        mockGameTimeAt12Mins = mock(GameTime.class);
        when(mockGameTimeAt12Mins.getActiveTime()).thenReturn(720000L);
        
        mockGameTimePast12Mins = mock(GameTime.class);
        when(mockGameTimePast12Mins.getActiveTime()).thenReturn(800000L);
    }
    
    @BeforeEach
    void setUp() {
        timeController = new TimeController(mockGameTimeStandard);
        timeControllerAt12Mins = new TimeController(mockGameTimeAt12Mins);
        timeControllerPast12Mins = new TimeController(mockGameTimePast12Mins);
        mockGameTimeDisplay = mock(GameTimeDisplay.class);
        timeController.registerTimeDisplay(mockGameTimeDisplay);
        timeControllerAt12Mins.registerTimeDisplay(mockGameTimeDisplay);
        timeControllerPast12Mins.registerTimeDisplay(mockGameTimeDisplay);
    }
    
    @Test
    void getTimeInSeconds() {
        assertEquals(300, timeController.getTimeInSeconds(),
                "300 seconds not given");
        assertEquals(720, timeControllerAt12Mins.getTimeInSeconds(),
                "720 seconds not given");
        assertEquals(800, timeControllerPast12Mins.getTimeInSeconds(),
                "800 seconds not given");
    }
    
    @Test
    void getTimeOfDay() {
        assertEquals(300000, timeController.getTimeOfDay(),
        "Incorrect time of day returned; 300000 not given.");
        assertEquals(0, timeControllerAt12Mins.getTimeOfDay(),
                "Incorrect time of day returned; 0 not given.");
        assertEquals(80000, timeControllerPast12Mins.getTimeOfDay(),
                "Incorrect time of day returned; 80000 not given.");
    }
    
    @Test
    void getHour() {
        assertEquals(10, timeController.getHour(),
                "Hour 10 not given.");
        assertEquals(0, timeControllerAt12Mins.getHour(),
                "Hour 0 not given.");
        assertEquals(2, timeControllerPast12Mins.getHour(),
                "Hour 2 not given.");
    }
    
    @Test
    void updateDisplay() {
        timeController.updateDisplay();
        verify(mockGameTimeDisplay, times(1)).update(10);
    }

    @Test
    void pauseUnpause() {
        timeController.pause();
        verify(mockGameTimeStandard, times(1)).getTime();
        
        timeController.unpause();
        verify(mockGameTimeStandard, times(1)).getTimeSince(40000L);
        verify(mockGameTimeStandard, times(1)).addPauseOffset(20000L);
    }
    
    @Test
    void setTime() {
        timeController.setTime(7);
        verify(mockGameTimeStandard, atLeastOnce()).getActiveTime();
    }
}