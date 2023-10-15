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
    ATTACK_MISS("sounds/weapons/SwordSwing.mp3"),
    ATTACK_HIT("sounds/weapons/SwordHitEntity.mp3"),
    GUN_ATTACK("sounds/weapons/GunAttack.mp3"),
    INVENTORY_OPEN("sounds/open-bag-sound-effect.mp3"),
    HOTKEY_SELECT("sounds/take-item-sound-effect.mp3"),
    CHICKEN_FEED("sounds/animals/ChickenFeed.mp3"),
    COW_FEED("sounds/animals/CowFeed.mp3"),
    ASTROLOTL_FEED("sounds/animals/AstrolotlFeed.mp3"),
    TAMED_ANIMAL("sounds/animals/TamedAnimal.mp3");

    private final String filePath;

    EffectSoundFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
}
