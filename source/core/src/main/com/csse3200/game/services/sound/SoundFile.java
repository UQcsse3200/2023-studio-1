package com.csse3200.game.services.sound;

public interface SoundFile {
    /**
     * A filepath that is to be loaded into the sound system.
     * Every enum that implements the SoundFile interface must have
     *  a private filePath variable to store this property.
     * @return a String filepath
     */
    public String getFilePath();
}
