package com.csse3200.game.services.sound;

import java.util.List;

/**
 * A MusicService that handles playback for long background music files which are streamed from disk.
 * A maximum of 10 tracks can be loaded at any given time, with only one track playing.
 */
public class BackgroundMusicService implements MusicService {

    public BackgroundMusicService() {

    }

    @Override
    public long play(SoundFile sound, boolean looping) {

        return 0;
    }

    @Override
    public long play(SoundFile sound) {
        return 0;
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
