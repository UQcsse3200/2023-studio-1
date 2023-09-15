package com.csse3200.game.components.endcredits;

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
import com.csse3200.game.components.intro.IntroDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndCreditsDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EndCreditsDisplay.class);
    private static final float Z_INDEX = 2f;
    /**
     * The Image that forms the background of the page.
     */
    private Image background;
    private Table table;
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
     * The Image that contains the animated planet.
     */
    private Image planet;

    /**
     * The table that forms the basis for the layout of the textual elements on the screen.
     */
    private Table rootTable;

    /**
     * The TypingLabel that contains the story that is displayed on the screen.
     * TypingLabel is a superclass of Label, which allows Label text to be animated.
     */
    private TypingLabel storyLabel;

    public EndCreditsDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        background = new Image(new Texture(Gdx.files.internal("images/intro_background_v2.png")));
        background.setPosition(0, 0);
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);

        // Load the animated planet
        planet = new Image(new Texture(Gdx.files.internal("images/intro_planet.png")));

        // Scale it to a 10% of screen width with a constant aspect ratio
        float planetWidth = (float) (Gdx.graphics.getWidth() * 0.1);
        float planetHeight = planetWidth * (planet.getHeight() / planet.getWidth());
        planet.setSize(planetWidth, planetHeight); // Set to a reasonable fixed size

        // The planet's speed is variable, it adjusts itself to make the planet appear above the text at the right time
        // The planet is placed at some offset above the screen in the center of the screen
        float planetOffset = 2500;
        planet.setPosition((float)Gdx.graphics.getWidth()/2, planetOffset, Align.center);


        // The {TOKENS} in the String below are used by TypingLabel to create the requisite animation effects
        String credits = """
                {WAIT=0.5}
                These are the end credits of the game.
                
                {WAIT}
                Hopefully this is appearing on the screen.
                {WAIT=0.5}
                
                I can't think of anything else to write.
                {WAIT}
                
                I'm just gonna keep writing to take up the space.
                {WAIT}
                
                Maybe I can change the text colour based off of win/lose conditions. 
                {WAIT=1}
                
                {COLOR=green}Here's an example of winning text!
                {WAIT=1}
                
                {COLOR=red}Here's and example of losing text!
                {WAIT=1}
                """;
        storyLabel = new TypingLabel(credits, skin); // Create the TypingLabel with the formatted story
        // Reduce the animation speed of all text in the story.
        String defaultTokens = "{SLOWER}";
        storyLabel.setDefaultToken(defaultTokens);
        storyLabel.setAlignment(Align.center); // Center align the text

        TextButton returnButton = new TextButton("Return To Main Menu", skin);

        returnButton.setVisible(true); // Make the continue button invisible

        // The continue button lets the user proceed to the main game
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Player Returned to Main Menu");
                returnToMenu();
            }
        });

        rootTable = new Table();
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

    //private void exit() {
      //  game.setScreen(ScreenType.MAIN_MENU);
    //}

    /**
     * Starts the main game
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
        // on screen
        if (planet.getY(Align.center) >= storyLabel.getY(Align.top) + planetToTextPadding) {
            planet.setY(planet.getY() - spaceSpeed); // Move the planet
            background.setY(background.getY() - spaceSpeed); // Move the background
        }

        // Resize the planet to the new screen size, maintaining aspect ratio
        float planetWidth = (float) (Gdx.graphics.getWidth() * 0.1);
        float planetHeight = planetWidth * (planet.getHeight() / planet.getWidth());
        planet.setSize(planetWidth, planetHeight);

        // re-center the planet
        planet.setPosition((float)Gdx.graphics.getWidth()/2, planet.getY(Align.center), Align.center);

        // adjust the speed of movement based on screen size and current fps to ensure planet
        // hits position at the right time
        float calculatedSpeed = (planet.getY(Align.center) - storyLabel.getY(Align.top) + planetToTextPadding) /
                (textAnimationDuration * (float)Gdx.graphics.getFramesPerSecond());

        // The universal speed limit must be enforced
        if (calculatedSpeed > 1.5f) {
            spaceSpeed = 1.5f;
        } else {
            spaceSpeed = calculatedSpeed;
        }


        logger.debug(String.format("Space Speed: %s", spaceSpeed));


        //stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        background.clear();
        table.clear();
        super.dispose();
    }

}