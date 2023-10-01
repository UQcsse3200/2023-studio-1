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

public class EndCreditsDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EndCreditsDisplay.class);
    private static final float Z_INDEX = 2f;
    private Image background;
    private final GdxGame game;
    private TextButton returnButton;
    private TypingLabel creditsLabel;

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
        background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/intro_background_v2.png", Texture.class));
        //background = new Image(new Texture(Gdx.files.internal("images/intro_background_v2.png")));
        background.setPosition(0, 0);
        float scaledHeight = Gdx.graphics.getWidth() * (background.getHeight() / background.getWidth());
        background.setHeight(scaledHeight);
        String credits = getCredits();
        creditsLabel = new TypingLabel(credits, skin);
        creditsLabel.setAlignment(Align.center);
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
        rootTable.add(creditsLabel).top().padTop((float) (Gdx.graphics.getHeight() * 2.5));
        rootTable.row().padTop(30f);
        rootTable.add(returnButton).bottom().padBottom(30f);
        // The background and planet are added directly to the stage so that they can be moved and animated freely.
        stage.addActor(background);
        stage.addActor(rootTable);
    }

    private String getCredits() {
        return """
                    {SLOW}
                    {RAINBOW}GARDENERS OF THE GALAXY: LEGEND OF THE ASTROHOE {ENDRAINBOW}
                    
                    Developed by Studio One
                      
                      
                    
                    {WAVE}{COLOR=red} TEAM ONE {CLEARCOLOR}{ENDWAVE}
                                   
                    Farming tools (Traktor)
                    Saving and Loading
                    Pausing Mechanics
                    Fences and Sprinklers
                        
                        
                        
                    {WAVE}{COLOR=green} TEAM TWO {CLEARCOLOR}{ENDWAVE}
                   
                    Player movement animations
                    Player quests and story progression
                    Ship and player artifacts
        
        
        
                    {WAVE}{COLOR=blue} TEAM THREE {CLEARCOLOR}{ENDWAVE}
                   
                    Map generation
                    Entity movement mechanics
                    Win and lose conditions
                    
                    
                    
                    {WAVE}{COLOR=gray} TEAM FOUR {CLEARCOLOR}{ENDWAVE}
                   
                    Farming animal animations and mechanics
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
                    Harvesting mechanics
                    
                    
                    
                    {WAVE}{COLOR=purple} TEAM SEVEN {CLEARCOLOR}{ENDWAVE}
                   
                    Tile system
                    Weather system
                    Seeds and fertiliser
                    Day and night cycle
        
        
        
                    {WAVE}{COLOR=pink} TEAM EIGHT {CLEARCOLOR}{ENDWAVE}
                   
                    Player inventory system
                    Generic inventory system
                    Win and loss screens""";
    }

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