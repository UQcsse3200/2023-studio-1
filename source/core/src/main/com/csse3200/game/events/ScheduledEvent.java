package com.csse3200.game.events;

import java.util.List;

/**
 * Represents a scheduled event with associated information.
 *
 * <p>A scheduled event is an event that is scheduled to occur at a specific time in the future,
 * and it may carry a list of arguments to be passed when the event is triggered.
 *
 * @param eventName The name of the event.
 * @param args      A list of arguments to be passed when the event is triggered.
 * @param endTime   The time at which the event is scheduled to be triggered.
 */
public record ScheduledEvent(String eventName, List<Object> args, long endTime) {
}
