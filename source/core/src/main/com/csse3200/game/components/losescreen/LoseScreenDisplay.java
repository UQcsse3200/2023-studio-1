package com.csse3200.game.components.losescreen;

import com.csse3200.game.missions.quests.QuestFactory;
import com.csse3200.game.services.ServiceLocator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.csse3200.game.ui.UIComponent;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

/**
 * The display User Interface component for the losing screen
 */
public class LoseScreenDisplay extends UIComponent {
    /**
     * The logger instance for logging messages and debugging information in the LoseScreenDisplay class.
     */
    private static final Logger logger = LoggerFactory.getLogger(LoseScreenDisplay.class);

    /**
     * The Z-index (rendering order) for the LoseScreenDisplay component within the game's UI.
     */
    private static final float Z_INDEX = 2f;

    /**
     * The background image displayed on the losing screen.
     */
    private Image background;

    /**
     * The main game instance used for screen transitions.
     */
    private final GdxGame game;

    /**
     * The button that allows the player to return to the main menu after losing the game.
     */
    private TextButton returnButton;

    /**
     * A flag indicating whether the losing screen animation has finished.
     */
    private boolean animationFinished = false;

    /**
     * The speed at which the losing screen elements move during the animation.
     */
    private float spaceSpeed = 0.33f;

    /**
     * The image of the planet displayed on the losing screen.
     */
    private Image planet;

    /**
     * The TypingLabel used to display the losing message text.
     */
    private TypingLabel storyLabel;

    /**
     * The message displayed on the losing screen, explaining the reason for the player's defeat.
     */
    public static String losingMessage;

    /**
     * Creates a new LoseScreenDisplay instance.
     *
     * @param game The GdxGame instance.
     */
    public LoseScreenDisplay(GdxGame game) {
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
        planet = background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/dead_planet2.png", Texture.class));
        // Scale it to a 10% of screen width with a constant aspect ratio
        float planetWidth = (float) (Gdx.graphics.getWidth() * 0.15);
        float planetHeight = planetWidth * (planet.getHeight() / planet.getWidth());
        planet.setSize(planetWidth, planetHeight); // Set to a reasonable fixed size
        // The planet's speed is variable, it adjusts itself to make the planet appear above the text at the right time
        // The planet is placed at some offset above the screen in the center of the screen
        float planetOffset = 2500;
        planet.setPosition((float)Gdx.graphics.getWidth()/2, planetOffset, Align.center);
        storyLabel = new TypingLabel(losingMessage, skin); // Create the TypingLabel with the formatted story
        storyLabel.setAlignment(Align.center); // Center align the text
        this.returnButton = new TextButton("Return To Main Menu", skin);
        this.returnButton.setVisible(false); // Make the continue button invisible
        // The continue button lets the user proceed to the main game
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
     * Sets the reason for losing the game to display in the losing message.
     *
     * @param causeOfDeath The cause of losing the game.
     */
    public static void setLoseReason(String causeOfDeath) {
        String reason = getString(causeOfDeath);
        losingMessage = """ 
                {SLOW}Despite your best efforts,
                
                """
                + reason +
                """
                
                
                {WAIT}Humanity has been lost to the ages.
                
                {COLOR=RED}Game Over
            """;
    }

    /**
     * Converts the cause of death into a corresponding losing message reason.
     *
     * @param causeOfDeath The cause of death.
     * @return The corresponding losing message reason.
     */
    @NotNull
    private static String getString(String causeOfDeath) {
        String reason;
        switch (causeOfDeath) {
            case "oxygen" -> reason = "your oxygen supply dwindles,\nand so does humanity's hope for survival.";
            case QuestFactory.ACT_I_MAIN_QUEST_NAME -> reason = "you failed to gain the Alien's trust in time,\nand so you were removed from the crash landing site.";
            case QuestFactory.ACT_II_MAIN_QUEST_NAME -> reason = "you failed to contact the rest of humanity in time,\nleaving you stranded and alone on an Alien world.";
            case QuestFactory.ACT_III_MAIN_QUEST_NAME -> reason = "you failed to make the planet liveable for humanity,\nleading what remains of the human race to their doom.";
            default -> reason = "Error Unknown Reason for Defeat";
        }
        return reason;
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
                (textAnimationDuration * (float)Gdx.graphics.getFramesPerSecond());
        // The universal speed limit must be enforced
        spaceSpeed = Math.min(calculatedSpeed, 1.5f);
        logger.debug(String.format("Space Speed: %s", spaceSpeed));
    }
    @Override
    public void dispose() {
        background.clear();
        super.dispose();
    }
}