package com.csse3200.game.components.cutscenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.ui.UIComponent;

public class CutsceneDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.missions.cutscenes.Cutscene.class);

    /**
     * The table that forms the basis for the layout of the cutscene
     */
    private Table table;

    private TypingLabel dialogueLabel;

    /**
     * Stores the dialogue text
     */
    private final String dialogue;

    // creates an instance of the cutscene class and assigns all variables as they need to be assigned - DOES NOT SPAWN THE ACTUAL CUTSCENE
    public CutsceneDisplay(String dialogue) {
        super();
        this.dialogue = dialogue;
    }

    @Override
    public void create() {
        super.create();
        this.spawnCutsceneDisplay();
    }

    // Creates the whole cutscene - to call other methods below this method
    public void spawnCutsceneDisplay() {
        System.out.println("CUTSCENE DISPLAY SPAWNED");
        logger.debug("Cutscene Display spawned");

        System.out.println("CUTSCENE TABLE SPAWNED");
        logger.debug("Cutscene table spawned");
        table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.padBottom(40);

        stage.addActor(table);
        this.spawnDialogueBox();
        this.spawnContinueButton();
        //stage.addActor(table);
    }

    // Spawns the sprites/entities that will be on the left/right side of the screen
    public void spawnSprites() {

    }

    // Spawn the dialogue box and populate it with text
    public void spawnDialogueBox () {
        System.out.println("CUTSCENE DIALOGUE SPAWNED");
        logger.debug("Cutscene dialogue spawned");

        dialogueLabel = new TypingLabel(this.dialogue, skin);
        dialogueLabel.setAlignment(Align.left);
        table.add(dialogueLabel).expandX();
    }

    public void spawnContinueButton() {
        System.out.println("CUTSCENE CONTINUE SPAWNED");
        logger.debug("Cutscene continue spawned");

        TextButton continueBtn = new TextButton("Continue", skin);
        continueBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Continue button clicked");
                        // add a line to trigger event when this btn is clicked
                    }
                });

        table.row();
        table.add(continueBtn);//.padTop(40f); // change 40f to whatever is needed
    }

    // animate the sprites/entities on the left/right sides of the screen
    public void animateSprites() {

    }

    // Ends the cutscene
    public void DisposeCutscene() {
        table.clear();
        super.dispose();
    }

    @Override
    public void dispose() {
        this.DisposeCutscene();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // handled by the stage
    }
}

