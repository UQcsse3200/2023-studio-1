package com.csse3200.game.missions.cutscenes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.screens.MainGameScreen.*;
import com.csse3200.game.services.ServiceLocator;
import net.dermetfan.gdx.CutsceneManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.ui.UIComponent;

public class Cutscene extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(Cutscene.class);

    private Table table = new Table();

    // creates an instance of the cutscene class and assigns all variables as they need to be assigned - DOES NOT SPAWN THE ACTUAL CUTSCENE
    public Cutscene() {
        //
    }

    // Creates the whole cutscene - to call other methods below this method
    public void spawnCutscene() {
        System.out.println("CUTSCENE SPAWNED");
    }

    // pauses the game
    public void pauseGame() {

    }

    // Spawns the sprites/entities that will be on the left/right side of the screen
    public void spawnSprites() {

    }

    // Spawn the dialogue box and populate it with text
    public void spawnDialogueBox () {

    }

    public void spawnContinueButton() {
        TextButton continueBtn = new TextButton("Continue", skin);
        continueBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Continue button clicked");
                        // add a line to trigger event when this btn is clicked
                    }
                });

        table.add(continueBtn).padTop(40f); // change 40f to whatever is needed
        table.row();
    }

    // animate the sprites/entities on the left/right sides of the screen
    public void animateSprites() {

    }

    // Ends the cutscene
    public void endCutscene() {
        table.clear();
        super.dispose();
    }

    // unpauses the game
    public void unPauseGame() {
        logger.debug("Setting paused state to: 1");
        // 1 is for delta time to run in normal speed
        ServiceLocator.getTimeSource().setTimeScale(1);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // handled by the stage
    }
}
