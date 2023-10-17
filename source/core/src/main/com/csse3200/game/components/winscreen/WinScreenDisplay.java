package com.csse3200.game.components.winscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The display User Interface component for the winning screen
 */
public class WinScreenDisplay extends UIComponent {
    /**
     * The logger instance for logging messages and debugging information in the WinScreenDisplay class.
     */
    private static final Logger logger = LoggerFactory.getLogger(WinScreenDisplay.class);

    /**
     * The Z-index (rendering order) for the WinScreenDisplay component within the game's UI.
     */
    private static final float Z_INDEX = 2f;

    /**
     * The background image displayed on the winning screen.
     */
    private Image background;

    /**
     * The main game instance used for screen transitions.
     */
    private final GdxGame game;

    /**
     * The button that allows the player to return to the main menu after winning the game.
     */
    private TextButton returnButton;

    /**
     * A flag indicating whether the winning screen animation has finished.
     */
    private boolean animationFinished = false;

    /**
     * The speed at which the winning screen elements move during the animation.
     */
    private float spaceSpeed = 0.33f;

    /**
     * The image of the planet displayed on the winning screen.
     */
    private Image planet;

    /**
     * The TypingLabel used to display the winning message text.
     */
    private TypingLabel storyLabel;

    /**
     * Creates a new WinScreenDisplay instance.
     *
     * @param game The GdxGame instance.
     */
    public WinScreenDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adds UI elements and initializes the screen.
     */
    private void addActors() {
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/intro_background_v2.png", Texture.class));
        background.setPosition(0, 0);
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);
        // Load the animated planet
        planet =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/good_planet.png", Texture.class));
        // Scale it to a 10% of screen width with a constant aspect ratio
        float planetWidth = (float) (Gdx.graphics.getWidth() * 0.15);
        float planetHeight = planetWidth * (planet.getHeight() / planet.getWidth());
        planet.setSize(planetWidth, planetHeight); // Set to a reasonable fixed size
        // The planet's speed is variable, it adjusts itself to make the planet appear above the text at the right time
        // The planet is placed at some offset above the screen in the center of the screen
        float planetOffset = 2500;
        planet.setPosition((float)Gdx.graphics.getWidth()/2, planetOffset, Align.center);
        String credits = """
                {SLOW}{WAIT}After all of your valiant efforts, humanity has landed on the planet, a place they may yet get to call home.
                
                {WAIT}"Impressive, Human!" Jarrael exclaims.
                
                {WAIT}You have aided in humanity's escape from earth and have ensured their survival in {COLOR=#3ABE88}ALPHA CENTAURI{COLOR=WHITE}, {WAIT}for the time being...
                """;
        storyLabel = new TypingLabel(credits, skin); // Create the TypingLabel with the formatted story
        storyLabel.setAlignment(Align.center); // Center align the text
        this.returnButton = new TextButton("Return To Main Menu", skin,"orange");
        this.returnButton.setVisible(false); // Make the continue button invisible
        // The return button lets the user proceed to the main menu
        this.returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Player Returned to Main Menu");
                returnToMenu();
            }
        });
        Table rootTable = new Table();
        rootTable.setFillParent(true); // Make the table fill the screen
        rootTable.row();
        rootTable.add(storyLabel).expandX().center().padTop(150f); // The story label is at the top of the screen
        rootTable.row().padTop(30f);
        rootTable.add(returnButton).bottom().padBottom(30f);
        // The background and planet are added directly to the stage so that they can be moved and animated freely.
        stage.addActor(background);
        stage.addActor(planet);
        stage.addActor(rootTable);
    }

    /**
     * Return to the main menu screen
     */
    private void returnToMenu() {
        game.setScreen(ScreenType.MAIN_MENU);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void update() {
        // This movement logic is triggered on every frame, until the middle of the planet hits its target position
        float planetToTextPadding = 100;
        if (!animationFinished) {
            if (planet.getY(Align.center) >= storyLabel.getY(Align.top) + planetToTextPadding) {
                planet.setY(planet.getY() - spaceSpeed); // Move the planet
                background.setY(background.getY() - spaceSpeed); // Move the background
            } else {
                // Animation has finished
                animationFinished = true;
                returnButton.setVisible(true); // Make the return button visible
            }
        }
        // Resize the planet to the new screen size, maintaining aspect ratio
        float planetWidth = (float) (Gdx.graphics.getWidth() * 0.15);
        float planetHeight = planetWidth * (planet.getHeight() / planet.getWidth());
        planet.setSize(planetWidth, planetHeight);
        // re-center the planet
        planet.setPosition((float)Gdx.graphics.getWidth()/2, planet.getY(Align.center), Align.center);
        // adjust the speed of movement based on screen size and current fps to ensure planet
        float textAnimationDuration = 12;
        float calculatedSpeed = (planet.getY(Align.center) - storyLabel.getY(Align.top) + planetToTextPadding) /
                (textAnimationDuration * Gdx.graphics.getFramesPerSecond());
        // The universal speed limit must be enforced
        spaceSpeed = Math.min(calculatedSpeed, 1.5f);
        String log = String.format("Space Speed: %s", spaceSpeed);
        logger.debug(log);
    }

    @Override
    public void dispose() {
        background.clear();
        super.dispose();
    }
}