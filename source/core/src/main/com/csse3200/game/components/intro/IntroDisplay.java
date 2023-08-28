package com.csse3200.game.components.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

/**
 * Display and logic for the intro screen.
 * This screen runs automatically and does not have any user introduction.
 */
public class IntroDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(IntroDisplay.class);
    private final GdxGame game;

    private static float textAnimationDuration = 18;
    private float spaceSpeed = 1;
    private boolean isMoving = true;
    private Image background;
    private Image planet;
    private Table rootTable;

    private TypingLabel storyLabel;

    public IntroDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/intro_background.png", Texture.class));

        background.setPosition(0, 0);
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);

        planet =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/intro_planet.png", Texture.class));
        planet.setSize(200, 200);
        float planetOffset = textAnimationDuration * UserSettings.get().fps;
        planet.setPosition((float)Gdx.graphics.getWidth()/2, planetOffset, Align.center);

        String story = """
                {WAIT=0.5}
                Earth as we know it has been ravaged by war and plague.
                
                {WAIT}
                A tapestry of battlefields and massacres are all that remain.
                {WAIT=0.5}
                
                Humanity though... {WAIT=1} perseveres.
                {WAIT}
                
                We now look to the stars for hope, {WAIT=1} a new place to call home.
                {WAIT}
                
                We will find our salvation in Alpha Centauri.
                {WAIT=1}
                
                {COLOR=green}Your objective, great farmlord, is to tame this wild planet we now call home.{WAIT=1}
                """;
        String defaultTokens = "{SLOWER}";
        storyLabel = new TypingLabel(story, skin);
        storyLabel.setDefaultToken(defaultTokens);
        storyLabel.setAlignment(Align.center);

        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.setVisible(false);

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                startGame();
            }
        });

        storyLabel.setTypingListener(new TypingAdapter() {
            public void end() {
                continueButton.setVisible(true);
            }
        });

        rootTable = new Table();
        rootTable.setFillParent(true); // Make the table fill the screen

        rootTable.add(storyLabel).expandX().center().padTop(150f);
        rootTable.row().padTop(30f);
        rootTable.add(continueButton).bottom().padBottom(30f);

        stage.addActor(background);
        stage.addActor(planet);
        stage.addActor(rootTable);
    }

    private void startGame() {
        game.setScreen(ScreenType.MAIN_GAME);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void update() {
        if (isMoving && planet.getY(Align.center) >= storyLabel.getY(Align.top) + 150) {
            planet.setY(planet.getY() - spaceSpeed);
            background.setY(background.getY() - spaceSpeed);
        }
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }
}
