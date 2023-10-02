package com.csse3200.game.services.sound;

public enum BackgroundSoundFile implements SoundFile {
    // TODO: Add enum declarations for all background sound tracks
    ExampleTrack("sounds/BGM_03_mp3.mp3", BackgroundMusicType.NORMAL)
    ;

    private final String filePath;
    private final BackgroundMusicType type;

    BackgroundSoundFile(String filePath, BackgroundMusicType type) {
        this.filePath = filePath;
        this.type = type;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
    
    public BackgroundMusicType getType() {
        return type;
    }
}
