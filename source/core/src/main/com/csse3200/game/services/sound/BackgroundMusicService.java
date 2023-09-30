package com.csse3200.game.services.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * A MusicService that handles playback for long background music files which are streamed from disk.
 * A maximum of 10 tracks can be loaded at any given time, with only one track playing.
 */
public class BackgroundMusicService implements MusicService {
    
    private static final Logger logger = LoggerFactory.getLogger(BackgroundMusicService.class);
    
    /**
     * An array list of up to 10 background music SoundFiles.
     */
    private ArrayList<BackgroundSoundFile> tracks;
    
    private Map<BackgroundSoundFile, Music> loadedMusic;
    
    /**
     * A flag that controls whether background music is played.
     */
    private boolean muteStatus;
    
    private BackgroundSoundFile currentlyPlaying;

    public BackgroundMusicService() {
        logger.debug("Initialising BackgroundMusicService");
        this.tracks = new ArrayList<>();
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
                    this.currentlyPlaying = (BackgroundSoundFile) sound; //Update currently playing
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

    @Override
    public void pause(SoundFile sound) throws InvalidSoundFileException {
        if (sound != currentlyPlaying) {
        
        }
        loadedMusic.get(sound);
    }

    @Override
    public void stop(SoundFile sound) throws InvalidSoundFileException {

    }

    @Override
    public void setMuted(boolean muted) {

    }

    @Override
    public boolean isMuted() {
        return muteStatus;
    }

    @Override
    public boolean isPlaying(SoundFile sound) throws InvalidSoundFileException {
        return false;
    }
    
    
    @Override
    public void loadSounds(List<SoundFile> sounds) throws InvalidSoundFileException {
        logger.debug("Loading background music.");
        for (SoundFile sound : sounds) {
            if (sound instanceof BackgroundSoundFile) {
                logger.debug("Loading a background track.");
                tracks.add((BackgroundSoundFile) sound);
                Music music = Gdx.audio.newMusic(Gdx.files.internal(sound.getFilePath()));
                loadedMusic.put((BackgroundSoundFile) sound, music);
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
        if (currentlyPlaying != null) {
            loadedMusic.get(currentlyPlaying).stop();
        }
    }
}
