package com.csse3200.game.services.sound;

public class InvalidSoundFileException extends Exception {

    public InvalidSoundFileException() {
        super("The SoundFile provided is not an EffectSoundFile");
    }
    public InvalidSoundFileException(String errorMessage) {
        super(errorMessage);
    }
}