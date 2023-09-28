package com.csse3200.game.events;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.events.listeners.EventListener2;
import com.csse3200.game.events.listeners.EventListener3;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class EventHandlerTest {
  EventHandler handler;

  @BeforeEach
  void beforeEach() {
    handler = new EventHandler();
  }

  @Test
  void shouldTriggerEvent() {
    EventListener0 listener = mock(EventListener0.class);
    handler.addListener("event", listener);
    handler.trigger("event");
    verify(listener).handle();
  }

  @Test
  void shouldTriggerMultiple() {
    EventListener0 listener = mock(EventListener0.class);
    EventListener0 listener2 = mock(EventListener0.class);
    handler.addListener("event", listener);
    handler.addListener("event", listener2);
    handler.trigger("event");
    verify(listener).handle();
    verify(listener2).handle();
  }

  @Test
  void shouldTriggerCorrectEvent() {
    EventListener0 listener = mock(EventListener0.class);
    EventListener0 listener2 = mock(EventListener0.class);
    handler.addListener("event", listener);
    handler.addListener("event2", listener2);

    handler.trigger("event2");
    verify(listener2).handle();
    verify(listener, times(0)).handle();

    handler.trigger("event");
    verify(listener).handle();
    verifyNoMoreInteractions(listener2);
  }

  @Test
  void shouldHandleNoListeners() {
    handler.trigger("not-real-event");
  }

  @Test
  void shouldTriggerOneArg() {
    EventListener1<String> listener = (EventListener1<String>)mock(EventListener1.class);
    handler.addListener("event", listener);
    handler.trigger("event", "argument");
    verify(listener).handle("argument");
  }

  @Test
  void shouldTriggerTwoArg() {
    EventListener2<Integer, Boolean> listener = (EventListener2<Integer, Boolean>)mock(EventListener2.class);
    handler.addListener("event", listener);
    handler.trigger("event", 5, true);
    verify(listener).handle(5, true);
  }

  @Test
  void shouldTriggerThreeArg() {
    EventListener3<Integer, Float, Long> listener = (EventListener3<Integer, Float, Long>)mock(EventListener3.class);
    handler.addListener("event", listener);
    handler.trigger("event", 1, 2f, 3L);
    verify(listener).handle(1, 2f, 3L);
  }

  @Test
  void shouldFailIncorrectArgs() {
    handler.addListener("stringEvent", (String s) -> {});
    assertThrows(ClassCastException.class, () -> {
      handler.trigger("stringEvent", true);
    });
  }

  @Test
  void shouldScheduleEvents() {
    // Set up game time
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);

    // set up handler with GameTime
    EventHandler scheduledHandler = new EventHandler();

    // test that
    EventListener0 listener = mock(EventListener0.class);
    scheduledHandler.addListener("event", listener);
    scheduledHandler.scheduleEvent(5f, "event");
    scheduledHandler.update();
    verify(listener, times(0)).handle();

    // check not called after 4.999 seconds
    when(gameTime.getTime()).thenReturn(4999L);
    scheduledHandler.update();
    verify(listener, times(0)).handle();

    // check is called after 5 seconds
    when(gameTime.getTime()).thenReturn(5000L);
    scheduledHandler.update();
    verify(listener, times(1)).handle();

    // check not called multiple times with multiple updates
    scheduledHandler.update();
    verify(listener, times(1)).handle();
  }

  @Test
  void shouldScheduleWithOneArg() {
    // Set up game time
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);

    // set up handler with GameTime
    EventHandler scheduledHandler = new EventHandler();

    EventListener1<String> listener1 = (EventListener1<String>)mock(EventListener1.class);
    scheduledHandler.addListener("event", listener1);
    scheduledHandler.scheduleEvent(10f, "event", "argument");

    when(gameTime.getTime()).thenReturn(12000L);
    scheduledHandler.update();
    verify(listener1).handle("argument");
  }

  @Test
  void shouldScheduleWithTwoArg() {
    // Set up game time
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);

    // set up handler with GameTime
    EventHandler scheduledHandler = new EventHandler();

    EventListener2<Integer, Boolean> listener2 = (EventListener2<Integer, Boolean>)mock(EventListener2.class);
    scheduledHandler.addListener("event", listener2);
    scheduledHandler.scheduleEvent(11f, "event", 5, true);

    when(gameTime.getTime()).thenReturn(12000L);
    scheduledHandler.update();
    verify(listener2).handle(5, true);

  }
  @Test
  void shouldScheduleWithThreeArg() {
    // Set up game time
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);

    // set up handler with GameTime
    EventHandler scheduledHandler = new EventHandler();

    EventListener3<Integer, Float, Long> listener3 = (EventListener3<Integer, Float, Long>)mock(EventListener3.class);
    scheduledHandler.addListener("event", listener3);
    scheduledHandler.scheduleEvent(12f,"event", 1, 2f, 3L);

    when(gameTime.getTime()).thenReturn(12000L);
    scheduledHandler.update();
    verify(listener3).handle(1, 2f, 3L);
  }

  @Test
  void shouldCancelEvents() {
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);

    // set up handler with GameTime
    EventHandler scheduledHandler = spy(new EventHandler());

    ScheduledEvent event0 = scheduledHandler.scheduleEvent(1f, "event");
    ScheduledEvent event1 = scheduledHandler.scheduleEvent(2f, "event", 1);
    ScheduledEvent event2 = scheduledHandler.scheduleEvent(3f, "event", 1,2);
    ScheduledEvent event3 = scheduledHandler.scheduleEvent(4f, "event", 1,2,3);

    scheduledHandler.cancelEvent(event0);
    when(gameTime.getTime()).thenReturn(1000L);
    scheduledHandler.update();
    // already showed with previous tests triggering scheduled events works. Now check function isn't called
    verify(scheduledHandler, times(0)).trigger("event");

    scheduledHandler.cancelEvent(event1);
    when(gameTime.getTime()).thenReturn(2000L);
    scheduledHandler.update();
    verify(scheduledHandler, times(0)).trigger("event", 1);

    scheduledHandler.cancelEvent(event2);
    when(gameTime.getTime()).thenReturn(3000L);
    scheduledHandler.update();
    verify(scheduledHandler, times(0)).trigger("event", 1, 2);

    scheduledHandler.cancelEvent(event3);
    when(gameTime.getTime()).thenReturn(4000L);
    scheduledHandler.update();
    verify(scheduledHandler, times(0)).trigger("event", 1, 2, 3);
  }

  @Test
  void shouldNotScheduleEventsWithNoGameTime() {
    handler.update();

    assertNull(handler.scheduleEvent(1f, "event"));
    assertNull(handler.scheduleEvent(2f, "event", 1));
    assertNull(handler.scheduleEvent(3f, "event", 1,2));
    assertNull(handler.scheduleEvent(4f, "event", 1,2,3));
  }

  @Test
  void cancelNullEventNoError() {
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);

    // set up handler with GameTime
    EventHandler scheduledHandler = spy(new EventHandler());

    ScheduledEvent event0 = scheduledHandler.scheduleEvent(1f, "event");

    scheduledHandler.cancelEvent(null);
  }

  @Test
  void shouldCancelALlEvents() {
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);

    // set up handler with GameTime
    EventHandler scheduledHandler = spy(new EventHandler());

    ScheduledEvent event0 = scheduledHandler.scheduleEvent(1f, "event");
    ScheduledEvent event1 = scheduledHandler.scheduleEvent(2f, "event", 1);
    ScheduledEvent event2 = scheduledHandler.scheduleEvent(3f, "event", 1,2);
    ScheduledEvent event3 = scheduledHandler.scheduleEvent(4f, "event", 1,2,3);


    scheduledHandler.cancelAllEvents();

    when(gameTime.getTime()).thenReturn(4000L);
    scheduledHandler.update();
    verify(scheduledHandler, times(0)).trigger("event");
    verify(scheduledHandler, times(0)).trigger("event", 1);
    verify(scheduledHandler, times(0)).trigger("event", 1, 2);
    verify(scheduledHandler, times(0)).trigger("event", 1, 2, 3);
  }

}