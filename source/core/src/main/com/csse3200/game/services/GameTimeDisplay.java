package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component for displaying the current game time on the Main Game Screen.
 */
public class GameTimeDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(GameTimeDisplay.class);
    Table table = new Table();
    Group group = new Group();
    private Image clockImage;
    private Array<Image> planetImages;
    private Image planetImage;
    private Array<Label> timeLabels;
    private Label timeLabel;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();

        logger.debug("Adding listener to hourUpdate event");

        // Listen for the "hourUpdate" event to update the display
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateDisplay);
        // Initial update
        updateDisplay();
    }

    /**
     * Loads textures and initialises the UI components.
     */
    public void createTexture() {

        logger.debug("Game time display texture being created");

        // Load clock image
        clockImage = new Image(ServiceLocator.getResourceService().getAsset(
                "images/time_system_ui/clock_frame.png", Texture.class));

        // Load planet images for each hour
        planetImages = new Array<>();
        for (int i = 0; i < 24; i++) {
            planetImages.add(new Image(ServiceLocator.getResourceService().getAsset(
                    String.format("images/time_system_ui/indicator_%d.png", i), Texture.class)));
        }

        // Create labels for each hour
        timeLabels = new Array<>();
        for (int i = 0; i < 24; i++) {
            if (i == 0) {
                timeLabels.add(new Label("12 am", skin, "large"));
            } else if (i < 12) {
                timeLabels.add(new Label(String.format("%d am", i), skin, "large"));
            } else if (i == 12) {
                timeLabels.add(new Label("12 pm", skin, "large"));
            } else {
                timeLabels.add(new Label(String.format("%d pm", i - 12), skin, "large"));
            }
        }

        // Initialise planet image for 6 am
        planetImage = planetImages.get(6);
        planetImage.getColor().a = 1.0f; // Set the first image to be fully visible
    }

    /**
     * Updates the display to show the current game time and transition effects.
     */
    public void updateDisplay() {
        boolean firstRun = false;
        if (planetImages == null) {
            createTexture();
            firstRun = true;
        }

        int time = ServiceLocator.getTimeService().getHour();

        if (!firstRun) {
            // Creates a sequence of actions for the transition out of the current planet image
            SequenceAction sequence = Actions.sequence();

            // Fade out the current planet image
            sequence.addAction(Actions.fadeOut(0.5f));

            // Change the planet image to the new one
            sequence.addAction(Actions.run(() -> {
                planetImage.setDrawable(planetImages.get(time).getDrawable());
            }));

            // Fade in the new planet image
            sequence.addAction(Actions.fadeIn(0.5f));

            // Add the sequence of actions to the planetImage actor
            planetImage.addAction(sequence);
        }

        // Set the correct text for the time label & planet image, dependent on time.
        timeLabel = timeLabels.get(time);

        // Accounts for offset in the position of the text label to fit single digit and double-digit
        // times nicely.
        if (time == 0 || (10 <= time && time <= 12) || (22 <= time && time <= 23)) {
            timeLabel.setPosition(clockImage.getImageX() + 158f, clockImage.getImageY() + 139f);
        } else if ((1 <= time && time <= 9) || (13 <= time && time <= 21)) {
            timeLabel.setPosition(clockImage.getImageX() + 164f, clockImage.getImageY() + 139f);
        }

        logger.debug("Game time display updated");

    }

    /**
     * Draws the UI components.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        table.clear();
        group.clear();
        table.top().left();
        table.setFillParent(true);
        table.padTop(150f).padLeft(-100f);

        group.addActor(clockImage);
        group.addActor(planetImage);
        group.addActor(timeLabel);

        table.add(group).size(200);
        stage.addActor(table);
    }

    /**
     * Disposes of resources when no longer needed.
     */
    @Override
    public void dispose() {
        super.dispose();
        clockImage.remove();
        planetImage.remove();
        timeLabel.remove();
    }
}
