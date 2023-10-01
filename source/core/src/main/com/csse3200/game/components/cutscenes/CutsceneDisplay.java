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
     * The Image that contains the player sprite.
     */
    private Image playerSprite;
    /**
     * The Image that contains the npc sprite.
     */
    private Image npcSprite;
    private Image sprite;
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
        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.bottom();
        table.padBottom(160);

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

        stage.addActor(table);
        this.spawnSprites();
        this.spawnDialogueBox();
        this.spawnContinueButton();
    }

    private void spawnSprite(String filePath, String position, float sizeIncrease) {
        //Need to take following code out of function as memory leak occurring
        TextureAtlas atlas = ServiceLocator.getResourceService()
                .getAsset(filePath, TextureAtlas.class);
        TextureAtlas.AtlasRegion region = atlas.findRegion("default");
        if (region == null) {
            throw new IllegalArgumentException(filePath + " is an invalid filePath");
        }
        sprite = new Image(region);
        //
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
        //table.add(sprite);
        stage.addActor(sprite);
    }

    // Spawns the sprites/entities that will be on the left/right side of the screen
    public void spawnSprites() {
        spawnSprite("images/questgiver.atlas", "RIGHT",  0.15f);
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
        Graphics.DisplayMode active = Gdx.graphics.getDisplayMode();
        table.add(dialogueLabel).width(active.width - 700).expandX();
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
                        transparentRectangle.getColor().a = 0f;
                        disposeCutscene();
                        cutscene.endCutscene();
                    }
                });

        table.row();
        table.add(continueBtn);//.padTop(40f); // change 40f to whatever is needed
    }

    // Ends the cutscene
    public void disposeCutscene() {
        table.clear();
        sprite.clear();
        super.dispose();
    }

    @Override
    public void dispose() {
        this.disposeCutscene();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // handled by the stage
    }
}

