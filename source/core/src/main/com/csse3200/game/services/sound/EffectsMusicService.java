package com.csse3200.game.services.sound;

import java.util.List;

/**
 * A MusicService that handles playback for short (<=5s) sound effects that are played from memory.
 * All files must be in the WAV format to ensure consistent playback.
 * There is no limit on the number of sound effects that can be loaded,
 * but loading too many will cause performance issues, so please be judicious.
 */
public class EffectsMusicService implements MusicService {

    public EffectsMusicService() {

    }

    @Override
    public void play(SoundFile sound, boolean looping) {

    }

    @Override
    public void play(SoundFile sound) {

    }

    @Override
    public void pause(SoundFile sound) {

    }

    @Override
    public void stop(SoundFile sound) {

    }

    @Override
    public void setMuted(boolean muted) {

    }

    @Override
    public boolean isMuted() {
        return false;
    }

    @Override
    public boolean isPlaying(SoundFile sound) {
        return false;
    }

    @Override
    public void loadSounds(List<SoundFile> sounds) {

    }

    @Override
    public void dispose() {

    }
}
