package com.csse3200.game.services.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A MusicService that handles playback for long background music files which are streamed from disk.
 * A maximum of 10 tracks can be loaded at any given time, with only one track playing.
 */
public class BackgroundMusicService implements MusicService {
    
    private static final Logger logger = LoggerFactory.getLogger(BackgroundMusicService.class);
    /** The maximum number of tracks loadable at once. */
    private static final int MAX_TRACKS = 10;
    
    /** An array list of up to 10 background music SoundFiles. */
    private ArrayList<BackgroundSoundFile> tracks;
    
    /** A map of sound files to their corresponding Music instances. */
    private Map<BackgroundSoundFile, Music> loadedMusic;
    
    /** A map of music types to a list of the Music instances of that type. */
    private Map<BackgroundMusicType, ArrayList<Music>> categorisedMusic;
    
    /** The number of tracks loaded at a given time. */
    private int numLoaded;
    
    /** A flag that controls whether background music has volume. */
    private boolean muteStatus;
    
    /** A flag to determine whether background music is paused. */
    private boolean pauseStatus;
    
    /** Flag to determine whether/when the next song is to be played. */
    private boolean keepPlaying;
    
    /** The most recently played/playing SoundFile key. */
    private BackgroundSoundFile currentlyActive;
    
    /** The currently active Music instance. */
    private Music currentMusic;
    
    public BackgroundMusicService() {
        logger.debug("Initialising BackgroundMusicService");
        this.tracks = new ArrayList<>();
        this.loadedMusic = new HashMap<>();
        this.categorisedMusic = new HashMap<>();
        // Add a map key for every music type for later sorting of loaded music, to be stored
        // in the value array.
        for (BackgroundMusicType type : BackgroundMusicType.values()) {
            this.categorisedMusic.put(type, new ArrayList<>());
        }
        numLoaded = 0;
        this.muteStatus = false;
        this.pauseStatus = false;
        this.keepPlaying = true;
        this.currentlyActive = null;
        this.currentMusic = null;
    }
    
    /**
     * Begins playing loaded Music instances of the provided type. Will continuously play
     * songs at random of the given type until stop() is called. After which play will need to
     * be called again to resume playback.
     * @param type The type of background music to be played.
     * @throws IllegalStateException If there are no available Music instances of the type given.
     */
    public void play(BackgroundMusicType type) throws IllegalStateException {
        this.stopCurrentlyPlaying();
        this.keepPlaying = true;
        Random rand = new Random();
        if (categorisedMusic.get(type).isEmpty()) {
            throw new IllegalStateException("No tracks loaded of type " + type);
        } else {
            while (keepPlaying) { // While not stopped.
                if (currentMusic == null || (!currentMusic.isPlaying() && !pauseStatus)) {
                    // Select a random loaded song of the given type.
                    currentMusic = categorisedMusic.get(type)
                            .get(rand.nextInt(categorisedMusic.get(type).size()));
                    // Set the completion listener to clean up after end of playback.
                    setupCompletionListener(currentMusic);
                    if (this.isMuted()) { // Apply mute status
                        currentMusic.setVolume(0.0f);
                    } else {
                        currentMusic.setVolume(1.0f);
                    }
                    currentMusic.play(); // Play the music.
                }
            }
        }
    }
    // TODO - UNNECESSARY FUNCTION
    /**
     *
     * @param sound - An enum value that implements the SoundFile interface
     * @param looping - A flag to control if the sound loops
     * @return
     * @throws InvalidSoundFileException
     */
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
    
    // TODO - UNNECESSARY FUNCTION
    @Override
    public long play(SoundFile sound) throws InvalidSoundFileException {
        return this.play(sound, false);
    }
    
    // TODO - UNNECESSARY FUNCTION
    /**
     * Attempts to pause a specific track. This is more important for Effects. Use pause()
     * to pause whichever track is currently active.
     * @param sound - The SoundFile to pause.
     * @throws InvalidSoundFileException if the given SoundFile is not active.
     */
    @Override
    public void pause(SoundFile sound) throws InvalidSoundFileException {
        if (sound.equals(currentlyActive)) {
            loadedMusic.get(sound).pause();
            pauseStatus = true;
        } else {
            throw new InvalidSoundFileException("Given parameter is not currently playing");
        }
    }
    
    /**
     * Pauses whichever track is currently playing.
     */
    public void pause() {
        loadedMusic.get(currentlyActive).pause();
        pauseStatus = true;
    }
    
    /**
     * Unpauses current music if it is paused.
     */
    public void unPause() {
        if (pauseStatus) {
            loadedMusic.get(currentlyActive).play();
            pauseStatus = false;
        }
        // Do nothing if not paused.
    }
    
    /**
     * Attempts to stop playback for a given sound file.
     * @param sound - An enum value that implements the SoundFile interface.
     * @throws InvalidSoundFileException if the given sound file is not playing.
     */
    @Override
    public void stop(SoundFile sound) throws InvalidSoundFileException {
        if (sound != currentlyActive) {
            throw new InvalidSoundFileException("Attempting to stop sound that is not playing.");
        } else {
            stopCurrentlyPlaying();
        }
    }
    
    /**
     * Stops the currently active background music.
     */
    private void stopCurrentlyPlaying() {
        if (currentlyActive != null) {
            loadedMusic.get(currentlyActive).stop();
            keepPlaying = false;
            currentMusic = null;
        }
    }
    
    /**
     * Sets the mute status of the background music to the given boolean value.
     * True being muted.
     * @param muted - The boolean state to set
     */
    @Override
    public void setMuted(boolean muted) {
        this.muteStatus = muted;
    }
    
    /**
     * Returns the current mute status.
     * @return the current mute status.
     */
    @Override
    public boolean isMuted() {
        return this.muteStatus;
    }
    
    /**
     * Checks if the given sound file is playing.
     * @param sound - An enum value that implements the SoundFile interface
     * @return True if playing, false if not.
     * @throws InvalidSoundFileException if the sound file provided either isn't a
     * BackgroundSoundFile or hasn't been loaded.
     */
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
     * Note: Only loads first 10 songs in list. Creates a private map of BackgroundSoundTypes
     * as keys to a list of Music instances of that type.
     * @param sounds - A list of SoundFiles to be loaded into memory
     * @throws InvalidSoundFileException if the provided list contains any non-BackgroundSoundFile
     * instances.
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
                logger.debug("Categorising Music instance by BackgroundMusicType.");
                categorisedMusic.get(((BackgroundSoundFile) sound).getType()).add(music);
                numLoaded++;
            } else {
                throw new InvalidSoundFileException("Not an instance of BackgroundSoundFile");
            }
        }
    }
    
    /**
     * Sets up the completion listener for a Music instance to end playback, allowing the
     * next track to play.
     * @param music the Music instance to add the listener to.
     */
    private void setupCompletionListener(Music music) {
        music.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                music.stop();
            }
        });
    }
    
    /**
     * Disposes of all loaded Music instances.
     * Resets internally stored sound file maps.
     * To resume playback after calling this, loadSounds() must be called again.
     */
    @Override
    public void dispose() {
        logger.debug("Disposing loaded Music instances");
        this.currentMusic = null;
        this.currentlyActive = null;
        for (BackgroundSoundFile track : tracks) {
            loadedMusic.get(track).dispose();
        }
        this.tracks = new ArrayList<>();
        this.loadedMusic = new HashMap<>();
        this.categorisedMusic = new HashMap<>();
    }
}
