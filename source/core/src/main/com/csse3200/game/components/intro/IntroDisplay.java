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

    private Table rootTable;

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
        Image background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/intro_background.png", Texture.class));

        background.setPosition(0, 0);
        background.setHeight(Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth()));
        background.setWidth(Gdx.graphics.getWidth());


        String story = """
                {WAIT}
                Earth as we know it has been ravaged by war and plague.
                
                {WAIT}
                Our reckless pursuit of technology revealed our presence to the greater galaxy,
                inviting a slew of challengers for a divided Earth to face.
                
                {WAIT}
                These new visitors waged war against us,
                {WAIT}unleashing terrifying new energy weaponry against our primitive kinetic armaments.
                {WAIT}
                
                Earth is left a barren landscape,
                a tapestry of battlefields and massacres are all that remain.
                {WAIT}
                
                Humanity though... {WAIT} perseveres.
                {WAIT}
                
                We now look to the stars for hope, {WAIT} a new place to call home.
                {WAIT}
                
                We will find our salvation in Alpha Centauri,
                {WAIT}
                
                but first we must tame the wild planet that we now call home...{WAIT}
                """;
        String defaultTokens = "{SLOWER}";
        TypingLabel storyLabel = new TypingLabel(story, skin);
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
        rootTable.row();
        rootTable.add(storyLabel).expandX().center();
        rootTable.row().padTop(30f);
        rootTable.add(continueButton).bottom().padBottom(30f);

        stage.addActor(background);
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
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }
}
