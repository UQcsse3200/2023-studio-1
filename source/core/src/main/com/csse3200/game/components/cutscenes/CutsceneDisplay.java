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
    private static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.missions.cutscenes.Cutscene.class);
    /**
     * The table that forms the basis for the layout of the cutscene
     */
    private Table table;
    /**
     * Stores the cutscene object that created the cutscene display
     */
    private Cutscene cutscene;
    private Image transparentRectangle;
    /**
     * Stores the dialogue text
     */
    private final String dialogue;

    private Table dialogueTable;

    /**
     * Creates a cutscene display using the given parameters
     * @param dialogue the dialogue that will be displayed
     * @param cutscene the cutscene object that created the cutscene display
     */
    public CutsceneDisplay(String dialogue, Cutscene cutscene) {
        super();
        this.dialogue = dialogue;
        this.cutscene = cutscene;
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

        logger.debug("Cutscene table spawned");
        dialogueTable = new Table();
        dialogueTable.setFillParent(true);
        dialogueTable.setDebug(true);
        dialogueTable.bottom();
        dialogueTable.padBottom(160);

        this.dimScreen();

        this.spawnSprites();
        this.spawnDialogueBox();
        this.spawnContinueButton();

        stage.addActor(dialogueTable);
    }

    /**
     * Dims the screen
     */
    public void dimScreen() {
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
        transparentRectangle.getColor().a = 0.5f;
        stage.addActor(transparentRectangle);
    }

    private void placeSprite(Image sprite, String position, float sizeIncrease) {
        logger.debug("Image spawning");
        int xpos = 0, ypos = 0;
        if (position == "LEFT") {
            xpos = 50;
            ypos = 50;

        } else if (position == "RIGHT") {
            xpos = 1000;
            ypos = 150;
        } else {
            throw new IllegalArgumentException(position + " is an invalid position");
        }
        sprite.setPosition(xpos,ypos);
        float scaledWidth = (float) (Gdx.graphics.getWidth() * sizeIncrease);
        float scaledHeight = scaledWidth * (sprite.getHeight() / sprite.getWidth());
        sprite.setWidth(scaledWidth);
        sprite.setHeight(scaledHeight);
        dialogueTable.add(sprite).expandX().width(scaledWidth).expandY().height(scaledHeight);
        //stage.addActor(sprite);
    }

    // Spawns the sprites/entities that will be on the left/right side of the screen
    public void spawnSprites() {
        TextureAtlas.AtlasRegion region;
        //Spawn npc
        npcAtlas = ServiceLocator.getResourceService()
                .getAsset("images/questgiver.atlas", TextureAtlas.class);
        region = npcAtlas.findRegion("default");
        if (region == null) {
            throw new IllegalArgumentException("images/questgiver.atlas is an invalid filePath");
        }
        npcSprite = new Image(region);
        placeSprite(npcSprite, "RIGHT",  0.15f);
        //spawnSprite("images/player.atlas", "LEFT", 0.45f);
    }

    /**
     * Spawns the dialogue text label and populates it
     */
    public void spawnDialogueBox () {
        logger.debug("Cutscene dialogue spawned");

        TypingLabel dialogueLabel = new TypingLabel(this.dialogue, skin);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setWrap(true);
        //Window dialogueWindow = new Window("Alien", skin);
        Graphics.DisplayMode active = Gdx.graphics.getDisplayMode();
        dialogueTable.add(dialogueLabel).width(active.width - 700).expandX(); // need to make it so text always contains the same proportion of the screen
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
    }

    @Override
    public void draw(SpriteBatch batch) {
        // handled by the stage
    }
}

