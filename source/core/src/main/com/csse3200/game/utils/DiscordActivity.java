package com.csse3200.game.utils;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordActivity {
    private final DiscordRichPresence rich;

    /**
     * Constructor for Discord Activity
     */
    public DiscordActivity() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordRPC.discordInitialize("1160854151668957245", handlers, true, "");
        rich = new DiscordRichPresence.Builder("Gardens of the Galaxy").build();
        DiscordRPC.discordUpdatePresence(rich);
        updateLargeImage("https://raw.githubusercontent.com/UQcsse3200/2023-studio-1/main/source/core/assets/images/game_logo2.png");
    }

    /**
     * Update discord status message
     * @param message to update status with
     */
    public void updateDiscordStatus(String message) {
        if (this.rich != null) {
            rich.state = message;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    /**
     * Update discord activity small image
     * @param imageUrl to set for small image
     */
    public void updateSmallImage(String imageUrl) {
        if (this.rich != null) {
            rich.smallImageKey = imageUrl;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    /**
     * Update discord activity large image
     * @param imageUrl to set for large image
     */
    public void updateLargeImage(String imageUrl) {
        if (this.rich != null) {
            rich.largeImageKey = imageUrl;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    /**
     * Start timer for discord activity
     */
    public void startTimer() {
        if (this.rich != null) {
            rich.startTimestamp = System.currentTimeMillis() / 1000;
            DiscordRPC.discordUpdatePresence(rich);
        }
    }

    /**
     * Stop timer for discord activity
     */
    public void stopTimer() {
        if (this.rich != null) {
            rich.startTimestamp = 0;
        }
    }
}
