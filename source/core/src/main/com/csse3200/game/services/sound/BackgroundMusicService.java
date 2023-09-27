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
    public long play(SoundFile sound, boolean looping) throws InvalidSoundFileException {

        return 0;
    }

    @Override
    public long play(SoundFile sound) throws InvalidSoundFileException {
        return 0;
    }

    @Override
    public void pause(SoundFile sound) throws InvalidSoundFileException {

    }

    @Override
    public void stop(SoundFile sound) throws InvalidSoundFileException {

    }

    @Override
    public void setMuted(boolean muted) {

    }

    @Override
    public boolean isMuted() {
        return false;
    }

    @Override
    public boolean isPlaying(SoundFile sound) throws InvalidSoundFileException {
        return false;
    }

    @Override
    public void loadSounds(List<SoundFile> sounds) throws InvalidSoundFileException {

    }

    @Override
    public void dispose() {

    }
}
