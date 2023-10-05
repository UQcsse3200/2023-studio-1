package com.csse3200.game.components.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.missions.cutscenes.Cutscene;
import com.csse3200.game.services.ServiceLocator;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.ui.UIComponent;

public class CutsceneDisplay extends UIComponent {
    /**
     * The atlas for the npc sprite.
     */
    private TextureAtlas npcAtlas;
    /**
     * The Image that contains the npc sprite.
     */
    private Image npcSprite;
    /**
     * Logger to log events
     */
    private static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.missions.cutscenes.Cutscene.class);
    /**
     * Stores the cutscene object that created the cutscene display
     */
    private Cutscene cutscene;
    /**
     * Stores the transparent rectangle
     */
    private Image transparentRectangle;
    /**
     * Stores the dialogue text
     */
    private final String dialogue;
    /**
     * Stores the table used to display the dialogue
     */
    private Table dialogueTable;
    /**
     * Stores the cutscene type
     */
    private final Cutscene.CutsceneType cutsceneType;

    /**
     * Creates a cutscene display using the given parameters
     * @param dialogue the dialogue that will be displayed
     * @param cutscene the cutscene object that created the cutscene display
     */
    public CutsceneDisplay(String dialogue, Cutscene cutscene, Cutscene.CutsceneType cutsceneType) {
        super();
        this.dialogue = dialogue;
        this.cutscene = cutscene;
        this.cutsceneType = cutsceneType;
    }

    @Override
    public void create() {
        super.create();
        this.spawnCutsceneDisplay();
    }

    /**
     * Spawns all the UI elements for the cutscene display
     */
    public void spawnCutsceneDisplay() {
        logger.debug("Cutscene Display spawned");
        removeExternalUI();
        logger.debug("Cutscene table spawned");
        dialogueTable = new Table();
        dialogueTable.setFillParent(true);
        //dialogueTable.setDebug(true);
        dialogueTable.bottom();
        dialogueTable.padBottom(160);

        this.dimScreen();

        this.spawnSprite();
        this.spawnDialogueBox();
        this.spawnContinueButton();

        stage.addActor(dialogueTable);
    }

    /**
     * Dims the screen
     */
    private void dimScreen() {
        logger.debug("Screen dimmed");
        //Following code for making transparent rectangle from
        //https://stackoverflow.com/questions/44260510/is-it-possible-to-draw-a-transparent-layer-without-using-image-libgdx
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture transparentRecTex = new Texture(pixmap);
        pixmap.dispose();
        transparentRectangle = new Image(transparentRecTex);
        transparentRectangle.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        transparentRectangle.getColor().a = 0.65f;
        stage.addActor(transparentRectangle);
    }

    /**
     * Places the sprite in the table
     * @param sprite the sprite to be added to the table
     * @param sizeIncrease the scalar to be used to scale the sprite's size
     */
    private void placeSprite(Image sprite, float sizeIncrease) {
        logger.debug("Image spawning");
        float scaledWidth = (float) (Gdx.graphics.getWidth() * sizeIncrease);
        float scaledHeight = scaledWidth * (sprite.getHeight() / sprite.getWidth());
        sprite.setWidth(scaledWidth);
        sprite.setHeight(scaledHeight);
        dialogueTable.add(sprite).expandX().width(scaledWidth).expandY().height(scaledHeight);
    }

    /**
     * Spawns the Alien sprite
     */
    private void spawnSprite() {
        TextureAtlas.AtlasRegion region;
        //Spawn npc
        npcAtlas = ServiceLocator.getResourceService()
                .getAsset("images/questgiver.atlas", TextureAtlas.class);
        region = npcAtlas.findRegion("default");
        if (region == null) {
            throw new IllegalArgumentException("images/questgiver.atlas is an invalid filePath");
        }
        npcSprite = new Image(region);
        placeSprite(npcSprite,  0.15f);
    }

    /**
     * Spawns the dialogue text label and populates it
     */
    private void spawnDialogueBox () {
        logger.debug("Cutscene dialogue spawned");

        TypingLabel dialogueLabel = new TypingLabel(this.dialogue, skin);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setWrap(true);
        //Window dialogueWindow = new Window("Alien", skin);
        Graphics.DisplayMode active = Gdx.graphics.getDisplayMode();
        dialogueTable.add(dialogueLabel).width(active.width - 700).expandX(); // need to make it so text always contains the same proportion of the screen
    }

    /**
     * Spawns the continue button
     */
    private void spawnContinueButton() {
        System.out.println("CUTSCENE CONTINUE SPAWNED");
        logger.debug("Cutscene continue spawned");

        TextButton continueBtn = new TextButton("Continue", skin);
        continueBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Continue button clicked");
                        cutscene.endCutscene();
                    }
                });

        dialogueTable.row();
        dialogueTable.add();
        dialogueTable.add(continueBtn);
    }

    @Override
    public void dispose() {
        dialogueTable.clear();
        transparentRectangle.clear();
        npcSprite.clear();
        stage.getRoot().removeActor(transparentRectangle);
        stage.getRoot().removeActor(dialogueTable);
        super.dispose();
        recoverExternalUI();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // handled by the stage
    }

    /**
     * Removes the UI components on the screen so that cutscene is not so cluttered
     */
    public void removeExternalUI() {
        ServiceLocator.getPlantInfoService().getEvents().trigger("toggleOpen", false);
        ServiceLocator.getTimeService().getEvents().trigger("toggleClockDisplay", false);
    }

    /**
     * Recovers the UI components that were removed back onto the screen
     */
    public void recoverExternalUI() {
        ServiceLocator.getPlantInfoService().getEvents().
                trigger("toggleOpen", KeyboardPlayerInputComponent.getShowPlantInfoUI());
        ServiceLocator.getTimeService().getEvents().trigger("toggleClockDisplay", true);
    }
}

