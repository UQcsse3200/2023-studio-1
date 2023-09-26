package com.csse3200.game.services.sound;

public enum BackgroundSoundFile implements SoundFile {
    // TODO: Add enum declarations for all background sound tracks
    ;

    private final String filePath;

    BackgroundSoundFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
}
