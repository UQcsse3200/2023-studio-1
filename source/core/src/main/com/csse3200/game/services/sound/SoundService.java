package com.csse3200.game.services.sound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundService {

    /**
     * A reference to the game's background music service
     */
    private final BackgroundMusicService backgroundMusicService;
    /**
     * A reference to the game's effects music service
     */
    private final EffectsMusicService effectsMusicService;

    /**
     * Instantiate a new SoundService
     */
    public SoundService() {
        this.backgroundMusicService = new BackgroundMusicService();
        this.effectsMusicService = new EffectsMusicService();
    }

    /**
     * Get the BackgroundMusicService
     * @return A reference to the game's BackgroundMusicService
     */
    public BackgroundMusicService getBackgroundMusicService() {
        return this.backgroundMusicService;
    }

    /**
     * Get the EffectsMusicService
     * @return A reference to the game's EffectsMusicService
     */
    public EffectsMusicService getEffectsMusicService() {
        return this.effectsMusicService;
    }
}
