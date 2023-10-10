package com.csse3200.game.ui;
import com.csse3200.game.events.EventHandler;

public class UIService {
    private final EventHandler eventHandler;

    public UIService() {
        eventHandler = new EventHandler();
    }

    public EventHandler getEvents() {
        return eventHandler;
    }

}
