package com.csse3200.game.components.losescreen;

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
    private static final Logger logger = LoggerFactory.getLogger(LoseScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private Image background;
    private final GdxGame game;
    private TextButton returnButton;
    private boolean animationFinished = false;
    private float spaceSpeed = 0.33f;
    private Image planet;
    private TypingLabel storyLabel;
    public static String losingMessage;

    public LoseScreenDisplay(GdxGame game) {
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

    public static void setLoseReason(String causeOfDeath) {
        String reason = getString(causeOfDeath);
        losingMessage = """ 
                {SLOW}
                Despite your best efforts, Alpha Centauri remains a wasteland.
                
                {WAIT}
                """
                + reason +
                """
                
                {WAIT}
                
                You gave it your all, but in the end... {WAIT=1} it wasn't enough.
                {WAIT}
                
                This is the end of the human race.
                {WAIT}
                
                {COLOR=red}Game Over
                {WAIT=1}
            """;
    }

    @NotNull
    private static String getString(String causeOfDeath) {
        String reason;
        switch (causeOfDeath) {
            case "oxygen" -> reason = "As your oxygen supply dwindles, so does humanities hope for survival.";
            case "mission1" -> reason = "Failed mission 1.";
            case "mission2" -> reason = "Failed mission 2.";
            case "mission3" -> reason = "Failed mission 3.";
            case "mission4" -> reason = "Failed mission 4.";
            case "mission5" -> reason = "Failed mission 5.";
            case "mission6" -> reason = "Failed mission 6.";
            default -> reason = "default reason";
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