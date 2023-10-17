package com.csse3200.game.services.sound;

public enum EffectSoundFile implements SoundFile {
    // TODO: Add enum declarations for all effect sound tracks
    TRACTOR_HONK("sounds/car-horn-6408.mp3"),
    TRACTOR_START_UP("sounds/tractor-start-up.wav"),
    SHOVEL("sounds/shovel.wav"),
    HOE("sounds/hoe.wav"),
    SCYTHE("sounds/hoe.wav"),
    WATERING_CAN("sounds/watering-can.wav"),
    FISHING_CAST("sounds/fishing-cast.wav"),
    FISHING_CATCH("sounds/applause.wav"),
    PLACE("sounds/place.wav"),
    GATE_INTERACT("sounds/gate-interact.wav"),
    IMPACT("sounds/Impact4.ogg"),
    INVENTORY_OPEN("sounds/open-bag-sound-effect.mp3"),
    HOTKEY_SELECT("sounds/take-item-sound-effect.mp3"),
    SHIP_CLUE_SOLVED("sounds/ship-clue-solved.wav"),
    SHIP_INSTALL_PART("sounds/install-ship-part.wav"),
    SHIP_FEATURE_UNLOCKED("sounds/ship-feature-unlocked.wav"),
    SHIP_TELEPORT("sounds/ship-teleport.wav"),
    SHIP_EATER_ATTACK("sounds/ship-eater-attack.wav"),
    SHIP_EATER_HIDE("sounds/ship-eater-hide.wav");

    private final String filePath;

    EffectSoundFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
}
