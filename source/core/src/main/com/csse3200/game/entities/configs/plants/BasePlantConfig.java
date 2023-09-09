package com.csse3200.game.entities.configs.plants;

/**
 * Defines a basic set of properties stored in plant entity config files to be
 * loaded by the PlantFactory.
 */
public class BasePlantConfig {
    public int health = 0;
    public String name = "none";
    public String type = "none";
    public String description = "none";
    public float idealWaterLevel = 0;
    public int adultLifeSpan = 0;
    public int maxHealth = 0;
    public String[] soundsArray = null;

    public int sproutThreshold = 0;
    public int juvenileThreshold = 0;
    public int adultThreshold = 0;

    public String imageFolderPath = "";
}
