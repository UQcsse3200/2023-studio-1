package com.csse3200.game.events;

import java.util.List;

public record ScheduledEvent(String eventName, List<Object> args, long endTime) {
}
