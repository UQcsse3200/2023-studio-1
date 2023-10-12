package com.csse3200.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.GdxGame;

/** This is the launch class for the desktop game. Passes control to libGDX to run GdxGame(). */
public class DesktopLauncher {
  public static void main(String[] arg) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setTitle("Gardener's of the Galaxy");
    config.setWindowIcon(Files.FileType.Internal, "images/icon.png", "images/icon2.png", "images/icon3.png");
    config.setInitialBackgroundColor(Color.PURPLE);
    new Lwjgl3Application(new GdxGame(), config);
  }
}