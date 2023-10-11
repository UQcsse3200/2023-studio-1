package com.csse3200.game.components.intro;

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
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import java.security.SecureRandom;

/**
 * Display and logic for the intro screen.
 * This screen runs automatically and does not have any user introduction.
 */
public class IntroDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(IntroDisplay.class);
    private final GdxGame game;

    /**
     * The time in seconds that it takes for the narration animation to reach the trigger point,
     * at which the planet should enter the frame.
     */
    private static float textAnimationDuration = 12;

    /**
     * The speed in pixels/frame that the background and the planet should move at.
     */
    private float spaceSpeed = 0.5f;

    /**
     * The distance away from the top the text that the planet should stop in pixels.
     */
    private float planetToTextPadding = 100;

    /**
     * The Image that forms the background of the page.
     */
    private Image background;

    /**
     * The Image that contains the animated planet.
     */
    private Image planet;
    
    /**
     * The Image that contains the top half of the cockpit.
     */
    private Image cockpitTop;
    
    /**
     * The Image that contains the bottom half of the cockpit.
     */
    private Image cockpitBottom;

    /**
     * The table that forms the basis for the layout of the textual elements on the screen.
     */
    private Table rootTable;
    
    /**
     * The table that forms the basis for the layout of the cockpit's textual elements
     * on the screen.
     */
    private Table cockpitTable;

    /**
     * The TypingLabel that contains the story that is displayed on the screen.
     * TypingLabel is a superclass of Label, which allows Label text to be animated.
     */
    private TypingLabel storyLabel;

    private boolean titleSequenceFinished = false;

    private float[] cockpitBottomPosition = {0, 0};
    private float[] cockpitTopPosition = {0, 0};

    private float screenWidth;
    private float screenHeight;
    private float shakeFactor = 1f;

    public IntroDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adds the UI elements to the Display's screen.
     */
    private void addActors() {
        // Load the background starfield image.
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/intro_background_v2.png", Texture.class));
        background.setPosition(0, 0);
        // Scale the height of the background to maintain the original aspect ratio of the image
        // This prevents distortion of the image.
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);

        // Load the animated planet
        planet =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/intro_planet.png", Texture.class));
        // Scale it to a 10% of screen width with a constant aspect ratio
        float planetWidth = (float) (Gdx.graphics.getWidth() * 0.1);
        float planetHeight = planetWidth * (planet.getHeight() / planet.getWidth());
        planet.setSize(planetWidth, planetHeight); // Set to a reasonable fixed size

        // The planet's speed is variable, it adjusts itself to make the planet appear above the text at the right time
        // The planet is placed at some offset above the screen in the center of the screen
        float planetOffset = 2500;
        planet.setPosition((float) Gdx.graphics.getWidth() / 2, planetOffset, Align.center);


        // The {TOKENS} in the String below are used by TypingLabel to create the requisite animation effects
        String story = """
                {SLOWER}{WAIT=0.5}15 years ago, humanity fled earth due to a world-ending calamity.
                                
                {WAIT}The {COLOR=#3ABE88}MOTHERSHIP{COLOR=WHITE} was the largest vessel of human life which fled Earth, leading the last survivors of your people to {COLOR=#3ABE88}ALPHA CENTAURI{COLOR=WHITE}.
                                
                                
                {WAIT}Upon arrival, the {COLOR=#3ABE88}MOTHERSHIP{COLOR=WHITE} had been sending explorers to search the solar system for habitable planets.
                                
                {WAIT}You were meant to be one such individual.
                                
                                
                {WAIT=1}As you descend into the upper atmosphere, a powerful {COLOR=#3ABE88}SOLAR SURGE{COLOR=WHITE} fries your ship's electronics.
                                
                {WAIT=1}You plummet onto the planet's surface...
                """;
        storyLabel = new TypingLabel(story, skin); // Create the TypingLabel with the formatted story
        // Reduce the animation speed of all text in the story.
        String defaultTokens = "{SLOWER}";
        storyLabel.setDefaultToken(defaultTokens);
        storyLabel.setAlignment(Align.center); // Center align the text

        TextButton continueButton = new TextButton("Continue", skin);

        continueButton.setVisible(true); // Make the continue button invisible

        // The continue button lets the user proceed to the main game
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Continue button clicked");
                //startGame();
                continueButton.setVisible(false);
                storyLabel.setVisible(false);

                cockpitTop.setVisible(true);
                cockpitBottom.setVisible(true);

                planet.setPosition(Gdx.graphics.getWidth() / 2f, (cockpitTop.getY(Align.bottom) - cockpitBottom.getY(Align.top)) / 2f);

                logger.error("({}, {}) - Cockpit Top", cockpitTopPosition[0], cockpitTopPosition[1]);
                logger.error("({}, {}) - Cockpit Bottom", cockpitBottomPosition[0], cockpitBottomPosition[1]);
                titleSequenceFinished = true;
            }
        });

        rootTable = new Table();
        rootTable.setFillParent(true); // Make the table fill the screen

        rootTable.row();

        rootTable.add(storyLabel).expandX().center().padTop(150f); // The story label is at the top of the screen
        rootTable.row().padTop(30f);
        rootTable.add(continueButton).bottom().padBottom(30f);

        // The background and planet are added directly to the stage so that they can be moved and animated freely.
        stage.addActor(background);
        stage.addActor(planet);
        stage.addActor(rootTable);

        /*
        cockpitTable = new Table();
        cockpitTable.setFillParent(true);
        cockpitTable.debug();
        cockpitTable.add(cockpitTop).top();
        cockpitTable.row();
        cockpitTable.add(cockpitBottom).bottom();
        stage.addActor(cockpitTable);
        */


        cockpitTop = new Image(ServiceLocator.getResourceService()
                .getAsset("images/crash-animation/Cockpit_Top.png", Texture.class));
        cockpitTop.setVisible(false);

        cockpitBottom = new Image(ServiceLocator.getResourceService()
                .getAsset("images/crash-animation/Cockpit_Bottom.png", Texture.class));
        cockpitBottom.setVisible(false);

        stage.addActor(cockpitTop);
        stage.addActor(cockpitBottom);

        repositionCockpit();
    }

    /**
     * Starts the main game
     */
    private void startGame() {
        game.setScreen(ScreenType.MAIN_GAME);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    private void shake(float shakeFactor) {
        if (shakeFactor != 0) {
            float maxX = 20f;
            float maxY = 20f;

            SecureRandom random = new SecureRandom();

            boolean xDirection = random.nextBoolean();
            float xMagnitude = random.nextFloat() * (xDirection ? 1f : -1f);
            xMagnitude = xMagnitude * shakeFactor;

            boolean yDirection = random.nextBoolean();
            float yMagnitude = random.nextFloat() * (yDirection ? 1f : -1f);
            yMagnitude = yMagnitude * shakeFactor;

            float newCockpitTopX = cockpitTopPosition[0] + xMagnitude;
            float newCockpitTopY = cockpitTopPosition[1] + yMagnitude;

            float newCockpitBottomX = cockpitBottomPosition[0] + xMagnitude;
            float newCockpitBottomY = cockpitBottomPosition[1] + yMagnitude;

            if (Math.abs(cockpitTopPosition[0] - newCockpitTopX) < maxX
                    && Math.abs(cockpitTopPosition[1] - newCockpitTopY) < maxY
                    && Math.abs(cockpitBottomPosition[0] - newCockpitBottomX) < maxX
                    && Math.abs(cockpitBottomPosition[1] - newCockpitBottomY) < maxY) {
                cockpitTop.setPosition(newCockpitTopX, newCockpitTopY);
                cockpitBottom.setPosition(newCockpitBottomX, newCockpitBottomY);
            } else {
                repositionCockpit();
            }
        } else {
            repositionCockpit();
        }
    }

    private void repositionCockpit() {
        cockpitTop.setWidth(Gdx.graphics.getWidth() * 1.2f);
        cockpitTop.setHeight(Gdx.graphics.getHeight() * 0.4f);

        cockpitBottom.setWidth(Gdx.graphics.getWidth() * 1.2f);
        cockpitBottom.setHeight(Gdx.graphics.getHeight() * 0.4f);

        cockpitTop.setPosition( -Gdx.graphics.getWidth() * 0.1f,Gdx.graphics.getHeight(), Align.topLeft);
        cockpitTopPosition[0] = cockpitTop.getX();
        cockpitTopPosition[1] = cockpitTop.getY();

        cockpitBottom.setPosition(-Gdx.graphics.getWidth() * 0.1f, 0);
        cockpitBottomPosition[0] = cockpitBottom.getX();
        cockpitBottomPosition[1] = cockpitBottom.getY();
    }

    private void updateTitleAnimation() {
        // This movement logic is triggered on every frame, until the middle of the planet hits its target position
        // on screen
        if (planet.getY(Align.center) >= storyLabel.getY(Align.top) + planetToTextPadding) {
            planet.setY(planet.getY() - spaceSpeed); // Move the planet
            background.setY(background.getY() - spaceSpeed); // Move the background
        }

        // adjust the speed of movement based on screen size and current fps to ensure planet
        // hits position at the right time
        float calculatedSpeed = (planet.getY(Align.center) - storyLabel.getY(Align.top) + planetToTextPadding) /
                (textAnimationDuration * Gdx.graphics.getFramesPerSecond());

        // The universal speed limit must be enforced
        if (calculatedSpeed > 1.5f) {
            spaceSpeed = 1.5f;
        } else {
            spaceSpeed = calculatedSpeed;
        }

        String log = String.format("Space Speed: %s", spaceSpeed);
        logger.debug(log);
    }

    public void resizePlanetAnimation() {
        if (!titleSequenceFinished) {
            // Resize the planet to the new screen size, maintaining aspect ratio
            float planetWidth = (float) (Gdx.graphics.getWidth() * 0.1);
            float planetHeight = planetWidth * (planet.getHeight() / planet.getWidth());
            planet.setSize(planetWidth, planetHeight);

            // re-center the planet
            planet.setPosition((float)Gdx.graphics.getWidth()/2, planet.getY(Align.center), Align.center);
        } else {
            planet.setWidth(planet.getWidth() * 1.012f);
            planet.setHeight(planet.getHeight() * 1.012f);

            // re-center the planet
            planet.setPosition(Gdx.graphics.getWidth() / 2f,
                    ((cockpitTop.getY(Align.bottom) - cockpitBottom.getY(Align.top)) / 2f)
                            + cockpitBottom.getY(Align.top), Align.center);
        }
    }

    @Override
    public void update() {
        // TODO: Make the stars move again
        // TODO: Sort the naming of all the functions out
        float newWidth = Gdx.graphics.getWidth();
        float newHeight = Gdx.graphics.getHeight();

        if (screenWidth != newWidth || screenHeight != newHeight) {
            repositionCockpit();

            screenWidth = newWidth;
            screenHeight = newHeight;
        }

        resizePlanetAnimation();

        if (!titleSequenceFinished) {
            updateTitleAnimation();
        } else {
            shake(shakeFactor);
            shakeFactor = shakeFactor * 1.01f;
            //TODO call next screen
            if (shakeFactor >= 20f) {
                startGame();
            }
        }
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        rootTable.clear();
        planet.clear();
        background.clear();
        storyLabel.clear();
        super.dispose();
    }
}
