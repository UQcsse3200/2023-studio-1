package com.csse3200.game.services.sound;

public enum BackgroundSoundFile implements SoundFile {
    // TODO: Add enum declarations for all background sound tracks
    TEST_TRACK_1("sounds/background-music/Alien-Sky.mp3", BackgroundMusicType.NORMAL),
    TEST_TRACK_2("sounds/background-music/Where-The-Waves-Take-Us.mp3", BackgroundMusicType.NORMAL),
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
