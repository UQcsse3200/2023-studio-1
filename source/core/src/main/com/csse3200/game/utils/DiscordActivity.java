package com.csse3200.game.utils;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordActivity {
    private DiscordRichPresence rich;
    private DiscordEventHandlers handlers;

    public DiscordActivity() {
        handlers = new DiscordEventHandlers();
        DiscordRPC.discordInitialize("1160854151668957245", handlers, true, "");
        rich = new DiscordRichPresence.Builder("Gardens of the Galaxy").build();
        DiscordRPC.discordUpdatePresence(rich);
        updateLargeImage("https://raw.githubusercontent.com/UQcsse3200/2023-studio-1/main/source/core/assets/images/game_logo2.png");
    }
    public void updateDiscordStatus(String message) {
        if (this.rich != null) {
            rich.state = message;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    public void updateSmallImage(String message) {
        if (this.rich != null) {
            rich.smallImageKey = message;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    public void updateLargeImage(String message) {
        if (this.rich != null) {
            rich.largeImageKey = message;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    public void startTimer() {
        if (this.rich != null) {
            rich.startTimestamp = System.currentTimeMillis() / 1000;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    public void stopTimer() {
        if (this.rich != null) {
            rich.startTimestamp = 0;
        }
    }
}
