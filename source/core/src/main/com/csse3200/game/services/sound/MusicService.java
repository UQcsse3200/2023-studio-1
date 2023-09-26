package com.csse3200.game.services.sound;

import java.util.List;

/**
 * The MusicService interface is implemented by the BackgroundMusicService and the EffectMusicService
 * Both these classes have similar function definitions, however the implementations of these functions differs
 * based on the needs of the two types of audio played in the game.
 */
public interface MusicService {
    /**
     * Play a given SoundFile, with it optionally looping
     * @param sound - An enum value that implements the SoundFile interface
     * @param looping - A flag to control if the sound loops
     */
    public void play(SoundFile sound, boolean looping);

    /**
     * A convenience method to play a sound without explicitly controlling the loop property
     * @param sound - An enum value that implements the SoundFile interface
     */
    public void play(SoundFile sound);

    /**
     * Pause a SoundFile that is currently playing without losing the progress of that track.
     * If the sound is not currently playing, then this method fails transparently without throwing errors
     * (this will be logged on the debug channel).
     * @param sound - An enum value that implements the SoundFile interface
     */
    public void pause(SoundFile sound);

    /**
     * Stop a SoundFile that is currently playing. You will lose the progress of the track.
     * If the sound is not currently playing, then this method fails transparently without throwing errors
     * (this will be logged on the debug channel).
     * @param sound - An enum value that implements the SoundFile interface
     */
    public void stop(SoundFile sound);

    /**
     * Mutes the playback of a given MusicService.
     * @param muted - The boolean state to set
     */
    public void setMuted(boolean muted);

    /**
     * Check if the current MusicService is currently muted.
     * @return The current mute state of the MusicService
     */
    public boolean isMuted();

    /**
     * Checks if a certain SoundFile is playing.
     * @param sound - An enum value that implements the SoundFile interface
     * @return The playback status of a given SoundFile
     */
    public boolean isPlaying(SoundFile sound);

    /**
     * Given a list of SoundFiles, load them all into memory.
     * @param sounds - A list of SoundFiles to be loaded into memory
     */
    public void loadSounds(List<SoundFile> sounds);

    /**
     * Dispose all the currently loaded SoundFiles from memory before closing the service.
     * This may be moved to the deinit method of this class so that extra steps aren't needed otherwise.
     */
    public void dispose();
}
