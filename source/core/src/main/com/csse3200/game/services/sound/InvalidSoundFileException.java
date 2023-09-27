package com.csse3200.game.services.sound;

public class InvalidSoundFileException extends Exception {
    public InvalidSoundFileException(String errorMessage) {
        super(errorMessage);
    }
}