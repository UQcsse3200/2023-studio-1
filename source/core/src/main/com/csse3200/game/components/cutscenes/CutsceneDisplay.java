package com.csse3200.game.components.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.missions.cutscenes.Cutscene;
import com.csse3200.game.services.ServiceLocator;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.ui.UIComponent;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CutsceneDisplay extends UIComponent {

    /**
     * The Image that contains the npc sprite.
     */
    private Image npcSprite;

    /**
     * Logger to log events
     */
    private static final Logger logger = LoggerFactory.getLogger(CutsceneDisplay.class);
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
     * Stores the cutscene type
     */
    private  Window dialogueWindow;
    private final Cutscene.CutsceneType cutsceneType;

    private static final String DEFAULT = "default";

    /**
     * Creates a cutscene display using the given parameters
     *
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

        if (this.cutsceneType == Cutscene.CutsceneType.ALIEN) {
            dialogueWindow = new Window("Alien Jarrael", skin, "wooden");
        } else if (this.cutsceneType == Cutscene.CutsceneType.RADIO){
            dialogueWindow = new Window("Radio", skin, "wooden");
        } else if (this.cutsceneType == Cutscene.CutsceneType.PLACEABLE) {
            dialogueWindow = new Window("DJ Khaled's Ghost", skin, "wooden");
        }

        dialogueWindow.getTitleLabel().setAlignment(Align.center);
        dialogueWindow.bottom();
        dialogueWindow.setResizable(false);

        this.dimScreen();

        this.spawnSprite();
        this.spawnDialogueBox();
        this.spawnContinueButton();

        Graphics.DisplayMode active = Gdx.graphics.getDisplayMode();
        float dialogueWindowWidth = (float) (active.width * 0.5);
        float dialogueWindowHeight = (float) (active.height * 0.5);

        dialogueWindow.pack();
        dialogueWindow.setPosition(dialogueWindowWidth, dialogueWindowHeight, Align.center);
        stage.addActor(dialogueWindow);
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
        float scaledWidth = (Gdx.graphics.getWidth() * sizeIncrease);
        float scaledHeight = scaledWidth * (sprite.getHeight() / sprite.getWidth());
        sprite.setWidth(scaledWidth);
        sprite.setHeight(scaledHeight);
        dialogueWindow.add(sprite).expandX().width(scaledWidth).expandY().height(scaledHeight);
    }

    /**
     * Spawns the Alien sprite
     */
    private void spawnSprite() {
        TextureAtlas npcAtlas;
        TextureAtlas.AtlasRegion region;

        //Spawn sprite
        if (this.cutsceneType == Cutscene.CutsceneType.RADIO) {
            npcAtlas = ServiceLocator.getResourceService()
                    .getAsset("images/walkietalkie.atlas", TextureAtlas.class);
            region = npcAtlas.findRegion(DEFAULT);
        } else if (this.cutsceneType == Cutscene.CutsceneType.PLACEABLE) {
            npcAtlas = ServiceLocator.getResourceService()
                    .getAsset("images/GOD_IS_game_ver.atlas", TextureAtlas.class);
            region = npcAtlas.findRegion(DEFAULT);
        } else {
            npcAtlas = ServiceLocator.getResourceService()
                    .getAsset("images/questgiver.atlas", TextureAtlas.class);
            region = npcAtlas.findRegion(DEFAULT);
        }

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
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        TypingLabel dialogueLabel = new TypingLabel(this.dialogue, skin);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setWrap(true);
        dialogueLabel.setDefaultToken("{FAST}{COLOR=BLACK}");

        dialogueLabel.setTypingListener(new TypingAdapter() {
            @Override
            public void onChar(Character c) {
                if (Character.isLetter(c)) {
                    Sound shortBeep = Gdx.audio.newSound(Gdx.files.internal("sounds/beep.mp3"));
                    long id = shortBeep.play(0.1f);
                    List<Float> pitches = Arrays.asList(0.5f, 0.6f, 0.7f, 0.8f);
                    shortBeep.setPitch(id, pitches.get(random.nextInt(4)));
                }
            }
        });

        Graphics.DisplayMode active = Gdx.graphics.getDisplayMode();
        float dialogueLabelWidth = (float) (active.width * 0.6);
        dialogueWindow.add(dialogueLabel).width(dialogueLabelWidth).expandX();
    }

    /**
     * Spawns the continue button
     */
    private void spawnContinueButton() {
        logger.debug("Cutscene continue spawned");

        TextButton continueBtn = new TextButton("Continue", skin, "orange");
        continueBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Continue button clicked");
                        cutscene.endCutscene();
                    }
                });

        dialogueWindow.row();
        dialogueWindow.add();
        dialogueWindow.add(continueBtn);
    }

    @Override
    public void dispose() {
        dialogueWindow.clear();
        transparentRectangle.clear();
        npcSprite.clear();
        stage.getRoot().removeActor(transparentRectangle);
        stage.getRoot().removeActor(dialogueWindow);
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
        ServiceLocator.getUIService().getEvents().trigger("toggleUI", false);
    }

    /**
     * Recovers the UI components that were removed back onto the screen
     */
    public void recoverExternalUI() {
        ServiceLocator.getPlantInfoService().getEvents().
                trigger("toggleOpen", KeyboardPlayerInputComponent.getShowPlantInfoUI());
        ServiceLocator.getUIService().getEvents().trigger("toggleUI", true);
    }
}

