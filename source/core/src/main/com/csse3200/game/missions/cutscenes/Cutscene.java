package com.csse3200.game.missions.cutscenes;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.cutscenes.CutsceneDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cutscene{
    private static final Logger logger = LoggerFactory.getLogger(Cutscene.class);

    private Entity cutsceneEntity;

    /**
     * Stores the dialogue text
     */
    private final String dialogue;
    private final CutsceneType cutsceneType;
    /**
     * Creates an instance of the cutscene class and assigns all variables as they need to be assigned - DOES NOT SPAWN THE ACTUAL CUTSCENE
     * @param dialogue - the dialogue to be displayed in the cutscene
     */
    public Cutscene(String dialogue, CutsceneType cutsceneType) {
        super();
        this.dialogue = dialogue;
        this.cutsceneType = cutsceneType;
    }

    /**
     * Creates the whole cutscene - to call other methods below this method
     */
    public void spawnCutscene() {
        System.out.println("CUTSCENE SPAWNED");
        logger.info("Cutscene spawned");

        this.pauseGame();
        Stage stage = ServiceLocator.getRenderService().getStage();
        cutsceneEntity = new Entity();
        cutsceneEntity.addComponent(new CutsceneDisplay(dialogue, this, this.cutsceneType))
                .addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(cutsceneEntity);
    }

    /**
     * Pauses the game
     */
    public void pauseGame() {
        logger.info("Setting paused state to: 0");
        ServiceLocator.setCutSceneRunning(true);
        ServiceLocator.getTimeSource().setTimeScale(0);
    }

    /**
     * Unpauses the game
     */
    public void unPauseGame() {
        logger.info("Setting paused state to: 1");
        // 1 is for delta time to run in normal speed
        ServiceLocator.setCutSceneRunning(false);
        ServiceLocator.getTimeSource().setTimeScale(1);
    }

    /**
     * Tears down the cutscene
     */
    public void endCutscene() {
        cutsceneEntity.dispose();
        ServiceLocator.getEntityService().unregister(cutsceneEntity);
        this.unPauseGame();
    }

    /**
     * Represents what type if cutscene the cutscene will be
     */
    public enum CutsceneType {
        ALIEN, // Alien quest giver talks to player
        RADIO // Radio talks to player
    }
}