package com.csse3200.game.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.events.listeners.EventListener;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.events.listeners.EventListener2;
import com.csse3200.game.events.listeners.EventListener3;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Send and receive events between objects. EventHandler provides an implementation of the Observer
 * pattern, also known as an event system or publish/subscribe. When an event is triggered with
 * trigger(), all listeners are notified of the event.
 *
 * <p>Currently supports up to 3 arguments for an event. More can be added, but consider instead
 * passing a class with required fields.
 *
 * <p>If you get a ClassCastException from an event, trigger is being called with different
 * arguments than the listeners expect.
 */
public class EventHandler {
  private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
  private final List<ScheduledEvent> scheduledEvents = new ArrayList<>();
  Map<String, Array<EventListener>> listeners;
  private final GameTime timeSource;

  public EventHandler() {
    // Assume no events by default, which will be the case for most entities
    listeners = new HashMap<>(0);
    timeSource = ServiceLocator.getTimeSource();

    if (timeSource == null) {
      logger.debug("No instance of TimeSource found, cannot schedule events");
    }
  }

  /**
   * Add a listener to an event with zero arguments
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   */
  public void addListener(String eventName, EventListener0 listener) {
    registerListener(eventName, listener);
  }

  /**
   * Add a listener to an event with one argument
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   * @param <T> argument type
   */
  public <T> void addListener(String eventName, EventListener1<T> listener) {
    registerListener(eventName, listener);
  }

  /**
   * Add a listener to an event with two arguments
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   */
  public <T0, T1> void addListener(String eventName, EventListener2<T0, T1> listener) {
    registerListener(eventName, listener);
  }

  /**
   * Add a listener to an event with three arguments
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   * @param <T2> Type of arg 2
   */
  public <T0, T1, T2> void addListener(String eventName, EventListener3<T0, T1, T2> listener) {
    registerListener(eventName, listener);
  }

  /**
   * Trigger an event with no arguments
   *
   * @param eventName name of the event
   */
  public void trigger(String eventName) {
    logTrigger(eventName);
    forEachListener(eventName, (EventListener listener) -> ((EventListener0) listener).handle());
  }

  /**
   * Trigger an event with one argument
   *
   * @param eventName name of the event
   * @param arg0 arg to pass to event
   * @param <T> argument type
   */
  @SuppressWarnings("unchecked")
  public <T> void trigger(String eventName, T arg0) {
    logTrigger(eventName);
    forEachListener(
        eventName, (EventListener listener) -> ((EventListener1<T>) listener).handle(arg0));
  }

  /**
   * Trigger an event with one argument
   *
   * @param eventName name of the event
   * @param arg0 arg 0 to pass to event
   * @param arg1 arg 1 to pass to event
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   */
  @SuppressWarnings("unchecked")
  public <T0, T1> void trigger(String eventName, T0 arg0, T1 arg1) {
    logTrigger(eventName);
    forEachListener(
        eventName,
        (EventListener listener) -> ((EventListener2<T0, T1>) listener).handle(arg0, arg1));
  }

  /**
   * Trigger an event with one argument
   *
   * @param eventName name of the event
   * @param arg0 arg 0 to pass to event
   * @param arg1 arg 1 to pass to event
   * @param arg2 arg 2 to pass to event
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   * @param <T2> Type of arg 2
   */
  @SuppressWarnings("unchecked")
  public <T0, T1, T2> void trigger(String eventName, T0 arg0, T1 arg1, T2 arg2) {
    logTrigger(eventName);
    forEachListener(
        eventName,
        (EventListener listener) ->
            ((EventListener3<T0, T1, T2>) listener).handle(arg0, arg1, arg2));
  }

  /**
   * Schedule an event with no arguments
   *
   * @param delay delay before triggering event in seconds
   * @param eventName name of the event
   * @return the scheduled event
   */
  public ScheduledEvent scheduleEvent(float delay, String eventName) {
    if (timeSource == null) {
      logger.error("{} event not scheduled. No instance of TimeSource found", eventName);
      return null;
    }

    long endTime = timeSource.getTime() + (int)(delay * 1000);

    ScheduledEvent scheduledEvent = new ScheduledEvent(eventName, Collections.emptyList(), endTime);
    scheduledEvents.add(scheduledEvent);

    return scheduledEvent;
  }

  /**
   * Schedule an event with one argument
   *
   * @param delay delay before triggering event in seconds
   * @param eventName name of the event
   * @param arg0 arg to pass to event
   * @param <T> argument type
   * @return the scheduled event
   */
  public <T> ScheduledEvent scheduleEvent(float delay, String eventName, T arg0) {
    if (timeSource == null) {
      logger.error("{} event not scheduled. No instance of TimeSource found", eventName);
      return null;
    }

    long endTime = timeSource.getTime() + (int)(delay * 1000);

    ArrayList<Object> args = new ArrayList<>();
    args.add(arg0);

    ScheduledEvent scheduledEvent = new ScheduledEvent(eventName, args, endTime);
    scheduledEvents.add(scheduledEvent);
    return scheduledEvent;
  }

  /**
   * Schedule an event with two arguments
   *
   *
   * @param delay delay before triggering event in seconds
   * @param eventName name of the event
   * @param arg0 arg 0 to pass to event
   * @param arg1 arg 1 to pass to event
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   * @return the scheduled event
   */
  public <T0, T1> ScheduledEvent scheduleEvent(float delay, String eventName, T0 arg0, T1 arg1) {
    if (timeSource == null) {
      logger.error("{} event not scheduled. No instance of TimeSource found", eventName);
      return null;
    }

    long endTime = timeSource.getTime() + (int)(delay * 1000);

    ArrayList<Object> args = new ArrayList<>();
    args.add(arg0);
    args.add(arg1);

    ScheduledEvent scheduledEvent = new ScheduledEvent(eventName, args, endTime);
    scheduledEvents.add(scheduledEvent);

    return scheduledEvent;
  }

  /**
   * Schedule an event with three arguments
   *
   * @param delay delay before triggering event in seconds
   * @param eventName name of the event
   * @param arg0 arg 0 to pass to event
   * @param arg1 arg 1 to pass to event
   * @param arg2 arg 2 to pass to event
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   * @param <T2> Type of arg 2
   * @return the scheduled event
   */
  public <T0, T1, T2> ScheduledEvent scheduleEvent(float delay, String eventName, T0 arg0, T1 arg1, T2 arg2) {
    if (timeSource == null) {
      logger.error("{} event not scheduled. No instance of TimeSource found", eventName);
      return null;
    }

    long endTime = timeSource.getTime() + (int)(delay * 1000);

    ArrayList<Object> args = new ArrayList<>();
    args.add(arg0);
    args.add(arg1);
    args.add(arg2);

    ScheduledEvent scheduledEvent = new ScheduledEvent(eventName, args, endTime);
    scheduledEvents.add(scheduledEvent);

    return scheduledEvent;
  }

  /**
   * Trigger a scheduled event with given args
   *
   * @param scheduledEvent scheduled event to trigger
   */
  private void triggerScheduledEvent(ScheduledEvent scheduledEvent) {
    List<Object> args = scheduledEvent.args();
    String eventName = scheduledEvent.eventName();
      switch (args.size()) {
          case 0 -> trigger(eventName);
          case 1 -> trigger(eventName, args.get(0));
          case 2 -> trigger(eventName, args.get(0), args.get(1));
          case 3 -> trigger(eventName, args.get(0), args.get(1), args.get(2));
          default -> {
              // Nothing
          }
      }
  }

  /**
   * Update the event handler, processing and triggering scheduled events that have reached their
   * scheduled execution time.
   *
   * <p>If there is no instance of {@link GameTime} available, this method does nothing.
   */
  public void update() {
    if (timeSource == null) {
      return;
    }

    List<ScheduledEvent> eventsToTrigger = new ArrayList<>(scheduledEvents);
    eventsToTrigger.removeIf(event -> (timeSource.getTime() < event.endTime()));

    eventsToTrigger.forEach(this::triggerScheduledEvent);

    // remove in separate loop to avoid concurrent modification error
    scheduledEvents.removeIf(eventsToTrigger::contains);
  }

  /**
   * Cancels the given scheduled event
   * @param event event to cancel
   */
  public void cancelEvent(ScheduledEvent event) {
    scheduledEvents.remove(event);
  }

  /**
   * Cancels all scheduled events for an entity.
   */
  public void cancelAllEvents() {
    scheduledEvents.clear();
  }

  private void registerListener(String eventName, EventListener listener) {
    logger.debug("Adding listener {} to event {}", listener, eventName);
    Array<EventListener> eventListeners = listeners.getOrDefault(eventName, null);
    if (eventListeners == null) {
      eventListeners = new Array<>(1);
      listeners.put(eventName, eventListeners);
    }
    eventListeners.add(listener);
  }

  private void forEachListener(String eventName, Consumer<EventListener> func) {
    Array<EventListener> eventListeners = listeners.getOrDefault(eventName, null);
    if (eventListeners != null) {
      eventListeners.forEach(func);
    }
  }

  private static void logTrigger(String eventName) {
    logger.debug("Triggering event {}", eventName);
  }

  public Integer getScheduledEventsSize() {
    return scheduledEvents.size();
  }
}
