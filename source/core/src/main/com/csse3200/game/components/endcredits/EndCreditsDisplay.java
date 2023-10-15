package com.csse3200.game.components.endcredits;

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
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The display User Interface component for the credits screen
 */
public class EndCreditsDisplay extends UIComponent {
    /**
     * The logger instance for logging messages and debugging information in the EndCreditsDisplay class.
     */
    private static final Logger logger = LoggerFactory.getLogger(EndCreditsDisplay.class);

    /**
     * The Z-index (rendering order) for the EndCreditsDisplay component within the game's UI.
     */
    private static final float Z_INDEX = 2f;

    /**
     * The background image displayed on the credits screen.
     */
    private Image background;

    /**
     * The main game instance used for screen transitions.
     */
    private final GdxGame game;

    /**
     * The button that allows the player to return to the main menu after viewing the credits.
     */
    private TextButton returnButton;

    /**
     * The TypingLabel used to display the credits text.
     */
    private TypingLabel creditsLabel;

    private static final  String CREDITS = """
                {SLOW}
                {RAINBOW}GARDENERS OF THE GALAXY: LEGEND OF THE ASTROHOE {ENDRAINBOW}
                
                Developed by Studio One
                  
                  
                
                {WAVE}{COLOR=red} TEAM ONE {CLEARCOLOR}{ENDWAVE}
                               
                Items
                Tractor
                Saving and Loading
                Placeable objects
                Fireflies
                Fishing
                    
                    
                {WAVE}{COLOR=green} TEAM TWO {CLEARCOLOR}{ENDWAVE}
               
                Player movement animations
                Alien quest giver & mission UI
                Ship and player artifacts
                Ship Eaters
                Fish
                
                
                
                {WAVE}{COLOR=blue} TEAM THREE {CLEARCOLOR}{ENDWAVE}
               
                Map generation
                Entity movement modifiers
                Win and lose conditions
                Cutscenes
                
                
                
                {WAVE}{COLOR=gray} TEAM FOUR {CLEARCOLOR}{ENDWAVE}
               
                Farm animals
                Hostile animals
                Weapons system
                
                
                
                {WAVE}{COLOR=yellow} TEAM FIVE {CLEARCOLOR}{ENDWAVE}
               
                Screen and menu design
                Time system
                Oxygen system
                Sound system
                
                
                
                {WAVE}{COLOR=orange} TEAM SIX {CLEARCOLOR}{ENDWAVE}
               
                Plant design
                Plant growth mechanics
                
                
                
                {WAVE}{COLOR=purple} TEAM SEVEN {CLEARCOLOR}{ENDWAVE}
               
                Tile system
                Weather system
                Seeds and fertiliser
                Day and night cycle
                
                
                
                {WAVE}{COLOR=pink} TEAM EIGHT {CLEARCOLOR}{ENDWAVE}
               
                Player inventory system
                Generic inventory system
                Win and loss screens
                This






                
                {RAINBOW}Secret Khaled System™ {ENDRAINBOW}
                Team 1 and Team 2 
               
                """;

    /**
     * Creates a new EndCreditsDisplay instance.
     *
     * @param game The GdxGame instance.
     */
    public EndCreditsDisplay(GdxGame game) {
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
        creditsLabel = new TypingLabel(CREDITS, skin);
        creditsLabel.setAlignment(Align.center);
        // Create a "Return" button
        this.returnButton =  new TextButton("Return to Main Menu", skin);
        // Add a listener to the "Skip" button
        this.returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Player Returned to Main Menu");
                returnToMenu();
            }
        });
        // Set the position of the "Skip" button in the top-right corner
        this.returnButton.setPosition(Gdx.graphics.getWidth() - this.returnButton.getWidth() - 10, Gdx.graphics.getHeight() - this.returnButton.getHeight() - 10);
        Table rootTable = new Table();
        rootTable.setFillParent(true); // Make the table fill the screen
        rootTable.row();
        rootTable.add(creditsLabel).top().padTop((float) (Gdx.graphics.getHeight() * 2.5));
        // Add the "Skip" button to the stage
        stage.addActor(background);
        stage.addActor(returnButton);
        stage.addActor(rootTable);
    }

    /**
     * Returns to the main menu screen.
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
        // Calculate the new position for the storyLabel
        float spaceSpeed = 33.0f;
        float newY = creditsLabel.getY() + spaceSpeed * Gdx.graphics.getDeltaTime();
        // Check if the storyLabel has scrolled past the top of the screen
        if (newY >= Gdx.graphics.getHeight()) {
            // You can hide the text or do something when it exits the screen
            creditsLabel.setVisible(false);
            // The creditsLabel has scrolled off the screen, make the returnButton visible
            returnButton.setVisible(true);
            // Calculate the position to center the returnButton on the screen
            float buttonX = (Gdx.graphics.getWidth() - returnButton.getWidth()) / 2;
            float buttonY = (Gdx.graphics.getHeight() - returnButton.getHeight()) / 2;
            // Set the position of the returnButton
            returnButton.setPosition(buttonX, buttonY);
        } else {
            // Update the Y position of the storyLabel
            creditsLabel.setPosition(creditsLabel.getX(), newY);
        }
        // Update the stage
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        background.clear();
        super.dispose();
    }
}