package com.csse3200.game.services.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A MusicService that handles playback for short (less than or equal to 5s) sound effects that are played from memory.
 * All files must be in the WAV format to ensure consistent playback.
 * There is no limit on the number of sound effects that can be loaded,
 * but loading too many will cause performance issues, so please be judicious.
 */
public class EffectsMusicService implements MusicService {

    private static final Logger logger = LoggerFactory.getLogger(SoundService.class);

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
    public long play(SoundFile soundFile, boolean looping) throws InvalidSoundFileException {
        long playStatus = -1;
        if (!this.isMuted()) {
            if (soundFile instanceof EffectSoundFile) {
                if (this.loadedSounds.containsKey(soundFile)) {
                    Sound sound = this.loadedSounds.get(soundFile); // get the sound object for the chosen effect
                    long id = sound.play(); // play the sound
                    sound.setLooping(id, looping); // control whether the effect is looping
                    if (looping) { //only looping audio can be tracked
                        this.addPlayingSound((EffectSoundFile) soundFile, id); // log which effects are looping
                    }
                    playStatus = id; // set the return value to the id of the playing sound
                } else {
                    throw new InvalidSoundFileException("The SoundFile provided is not loaded");
                }
            } else {
                throw new InvalidSoundFileException();
            }
        } else {
            logger.debug("Playback is muted");
        }
        return playStatus;
    }

    @Override
    public long play(SoundFile sound) throws InvalidSoundFileException {
        return this.play(sound, false);
    }

    /**
     * Functionally identical to the {@link #stop(SoundFile) stop()} method for the EffectsMusicService
     * @param sound - An enum value that implements the SoundFile interface
     * @throws InvalidSoundFileException
     */
    @Override
    public void pause(SoundFile sound) throws InvalidSoundFileException {
        this.stop(sound);
    }

    @Override
    public void stop(SoundFile soundFile) throws InvalidSoundFileException {
        for (long id : this.getPlayingList((EffectSoundFile) soundFile)) {
            this.stop(soundFile, id);
        }
    }

    /**
     * Functionally similar to {@link #stop(SoundFile) stop(1), but only stops a single instance of a Sound from playing}
     * @param soundFile The SoundFile to stop playing
     * @param id The instance id of the sound to stop playing
     * @throws InvalidSoundFileException
     */
    public void stop(SoundFile soundFile, long id) throws InvalidSoundFileException {
        if (soundFile instanceof EffectSoundFile) {
            Sound sound = this.loadedSounds.get((EffectSoundFile) soundFile);
            sound.stop(id);
            this.removePlayingSound((EffectSoundFile) soundFile, id);
        } else {
            throw new InvalidSoundFileException();
        }
    }

    @Override
    public void setMuted(boolean muted) {
        this.muteStatus = muted;
    }

    @Override
    public boolean isMuted() {
        return this.muteStatus;
    }

    /**
     * Only tracks looping audio, as there is no method by which to check playing status of non-looping effects.
     * <p>
     *
     * </p>
     * {@inheritDoc}
     */
    @Override
    public boolean isPlaying(SoundFile sound) throws InvalidSoundFileException {
        boolean isPlaying = false;
        if (sound instanceof EffectSoundFile) {
            List<Long> playingList = this.getPlayingList((EffectSoundFile) sound);

            if (playingList != null) { // the sound effect may not be loaded?
                // if any sound of this type is playing then the array will not be empty
                isPlaying = playingList.isEmpty();
            }
        } else {
            throw new InvalidSoundFileException();
        }
        return isPlaying;
    }

    /**
     * Check if a certain instance of a SoundFile is playing.
     * Only tracks looping audio, as there is no method by which to check playing status of non-looping effects.
     * @param sound The SoundFile to check
     * @param id The instance id of the sound
     * @return Boolean status
     * @throws InvalidSoundFileException
     */
    public boolean isPlaying(SoundFile sound, long id) throws InvalidSoundFileException {
        boolean isPlaying = false;
        if (sound instanceof EffectSoundFile) {
            if (this.isPlaying(sound)) { // check if any sounds of that type are playing
                isPlaying = this.getPlayingList((EffectSoundFile) sound).contains(id);
            }
        } else {
            throw new InvalidSoundFileException();
        }
        return isPlaying;
    }

    @Override
    public void loadSounds(List<SoundFile> sounds) throws InvalidSoundFileException {
        for (SoundFile sound : sounds) {
            if (sound instanceof EffectSoundFile) {
                Sound loadedSound = Gdx.audio.newSound(Gdx.files.internal(sound.getFilePath()));
                this.playingSounds.put((EffectSoundFile) sound, new ArrayList<>()); // create a blank playing list
                this.loadedSounds.put((EffectSoundFile) sound, loadedSound);
            } else {
                throw new InvalidSoundFileException();
            }
        }
    }

    @Override
    public void dispose() {
        for (Sound sound : this.loadedSounds.values()) {
            sound.dispose();
        }
    }

    /**
     * Get the list of playing instances for a given sound file
     * @param soundFile The EffectSoundFile that needs to be checked
     * @return A list of sound instance ids
     */
    private List<Long> getPlayingList(EffectSoundFile soundFile) {
        return this.playingSounds.get(soundFile);
    }

    /**
     * Adds a sound id to the playing sounds data structure
     * @param soundFile the EffectSoundFile that was played
     * @param id the sound instance id
     */
    private void addPlayingSound(EffectSoundFile soundFile, long id) {
        this.getPlayingList(soundFile).add(id);
    }

    /**
     * Remove a single sound id from the playing sounds data structure
     * @param soundFile the EffectSoundFile that is playing
     * @param id the sound instance id
     */
    private void removePlayingSound(EffectSoundFile soundFile, long id) {
        this.getPlayingList(soundFile).remove(id);
    }
}
