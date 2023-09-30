package com.csse3200.game.services.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * A MusicService that handles playback for long background music files which are streamed from disk.
 * A maximum of 10 tracks can be loaded at any given time, with only one track playing.
 */
public class BackgroundMusicService implements MusicService {
    
    private static final Logger logger = LoggerFactory.getLogger(BackgroundMusicService.class);
    private static final int MAX_TRACKS = 10;
    
    /**
     * An array list of up to 10 background music SoundFiles.
     */
    private ArrayList<BackgroundSoundFile> tracks;
    
    private Map<BackgroundSoundFile, Music> loadedMusic;
    
    /**
     * The number of tracks loaded at a given time.
     */
    private int numLoaded;
    
    /**
     * A flag that controls whether background music is played.
     */
    private boolean muteStatus;
    
    private BackgroundSoundFile currentlyActive;

    public BackgroundMusicService() {
        logger.debug("Initialising BackgroundMusicService");
        this.tracks = new ArrayList<>();
        this.loadedMusic = new HashMap<>();
        numLoaded = 0;
        this.muteStatus = false;
    }

    @Override
    public long play(SoundFile sound, boolean looping) throws InvalidSoundFileException {
        logger.debug("Running checks on background music file.");
        if (!this.isMuted()) {
            if (sound instanceof BackgroundSoundFile) {
                logger.debug("Creating Music instance.");
                Music music = loadedMusic.get((BackgroundSoundFile) sound);
                if (music != null) { // If a loaded Key-Value pair is present for this sound file
                    logger.debug("Background music play checks successful. " +
                            "Attempting to play file");
                    music.setLooping(looping); // Set whether the track loops
                    music.play(); // Play the music instance of the sound file
                    this.currentlyActive = (BackgroundSoundFile) sound; //Update currently playing
                } else {
                    throw new InvalidSoundFileException("SoundFile not loaded.");
                }
            } else {
                throw new InvalidSoundFileException("Not an instance of BackgroundSoundFile");
            }
        }
        return 0; // Play ID not needed for background music
    }

    @Override
    public long play(SoundFile sound) throws InvalidSoundFileException {
        return this.play(sound, false);
    }
    
    /**
     * Attempts to pause a specific track. This is more important for Effects. Use pause()
     * to pause whichever track is currently active.
     * @param sound - The SoundFile to pause.
     * @throws InvalidSoundFileException if the given SoundFile is not active.
     */
    @Override
    public void pause(SoundFile sound) throws InvalidSoundFileException {
        if (sound == currentlyActive) {
            loadedMusic.get(sound).pause();
        } else {
            throw new InvalidSoundFileException("Given parameter is not currently playing");
        }
    }
    
    /**
     * Pauses whichever track is currently playing.
     */
    public void pause() {
        loadedMusic.get(currentlyActive).pause();
    }

    @Override
    public void stop(SoundFile sound) throws InvalidSoundFileException {
        if (sound != currentlyActive) {
        
        }
    }
    
    /**
     * Stops the currently active
     */
    public void stop() {
        loadedMusic.get(currentlyActive).stop();
    }

    @Override
    public void setMuted(boolean muted) {
        this.muteStatus = muted;
    }

    @Override
    public boolean isMuted() {
        return this.muteStatus;
    }

    @Override
    public boolean isPlaying(SoundFile sound) throws InvalidSoundFileException {
        if (sound instanceof BackgroundSoundFile
                && loadedMusic.get((BackgroundSoundFile) sound) != null) {
            return loadedMusic.get((BackgroundSoundFile) sound).isPlaying();
        } else {
            throw new InvalidSoundFileException("SoundFile not loaded or not an instance of " +
                    "BackgroundSoundFile.");
        }
    }
    
    /**
     * Note: Only loads first 10 songs in list.
     * @param sounds - A list of SoundFiles to be loaded into memory
     * @throws InvalidSoundFileException
     */
    @Override
    public void loadSounds(List<SoundFile> sounds) throws InvalidSoundFileException {
        logger.debug("Loading background music.");
        for (SoundFile sound : sounds) {
            if (numLoaded >= MAX_TRACKS) {
                logger.debug("10 music track maximum reached - further loading aborted");
                break;
            }
            if (sound instanceof BackgroundSoundFile) {
                logger.debug("Loading a background track.");
                tracks.add((BackgroundSoundFile) sound);
                Music music = Gdx.audio.newMusic(Gdx.files.internal(sound.getFilePath()));
                loadedMusic.put((BackgroundSoundFile) sound, music);
                numLoaded++;
            } else {
                throw new InvalidSoundFileException("Not an instance of BackgroundSoundFile");
            }
        }
    }

    @Override
    public void dispose() {
        logger.debug("Disposing loaded Music instances");
        for (BackgroundSoundFile track : tracks) {
            loadedMusic.get(track).dispose();
        }
    }
    
    /**
     * Stops the currently playing background music.
     */
    private void stopCurrentlyPlaying() {
        if (currentlyActive != null) {
            loadedMusic.get(currentlyActive).stop();
        }
    }
}
