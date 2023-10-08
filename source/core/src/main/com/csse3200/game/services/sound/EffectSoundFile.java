package com.csse3200.game.services.sound;

public enum EffectSoundFile implements SoundFile {
    // TODO: Add enum declarations for all effect sound tracks
    TRACTOR_HONK("sounds/car-horn-6408.mp3"),
    GATE_INTERACT("sounds/gate-interact.wav"),
    IMPACT("sounds/Impact4.ogg"),
    INVENTORY_OPEN("sounds/open-bag-sound-effect.mp3"),
    HOTKEY_SELECT("sounds/take-item-sound-effect.mp3");

    private final String filePath;

    EffectSoundFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
}
