package com.csse3200.game.services.sound;

import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A MusicService that handles playback for short (<=5s) sound effects that are played from memory.
 * All files must be in the WAV format to ensure consistent playback.
 * There is no limit on the number of sound effects that can be loaded,
 * but loading too many will cause performance issues, so please be judicious.
 */
public class EffectsMusicService implements MusicService {

    /**
     * EffectSoundFiles that are loaded in memory and associated Sound object.
     * These are the only sounds that can be played.
     */
    private Map<EffectSoundFile, Sound> loadedSounds;

    /**
     * Contains each loaded EffectSoundFile and the long reference to the playing instance
     */
    private Map<EffectSoundFile, List<Long>> playingSounds;

    /**
     * This flag controls whether anything is played by the sound system
     */
    private boolean muteStatus;

    public EffectsMusicService() {
        this.loadedSounds = new HashMap<>();
        this.playingSounds = new HashMap<>();
        this.muteStatus = false;
    }

    @Override
    public long play(SoundFile sound, boolean looping) throws InvalidSoundFileException {
        long playStatus = -2;
        if (!this.isMuted()) {

        } else {

        }
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
