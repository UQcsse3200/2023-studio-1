package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying the current game time on the Main Game Screen.
 */
public class GameTimeDisplay extends UIComponent {

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
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateDisplay);
        updateDisplay();
    }

    public void createTexture() {
        clockImage = new Image(ServiceLocator.getResourceService().getAsset(
                "images/time_system_ui/clock_frame.png", Texture.class));
        planetImages = new Array<>();
        for (int i = 0; i < 24; i++) {
            planetImages.add(new Image(ServiceLocator.getResourceService().getAsset(
                    String.format("images/time_system_ui/indicator_%d.png", i), Texture.class)));
        }
        timeLabels = new Array<>();
        for (int i = 0; i < 24; i++) {
            if (i == 0) {
                timeLabels.add(new Label("12 am", skin, "large"));
            } else if (i >= 1 && i < 12) {
                timeLabels.add(new Label(String.format("%d am", i), skin, "large"));
            } else if (i == 12) {
                timeLabels.add(new Label("12 pm", skin, "large"));
            } else {
                timeLabels.add(new Label(String.format("%d pm", i - 12), skin, "large"));
            }
        }
    }

    public void updateDisplay() {
        if (planetImages == null) {
            createTexture();
        }
        int time = ServiceLocator.getTimeService().getHour();
        // Set the correct text for the time label & planet image, dependent on time.
        timeLabel = timeLabels.get(time);
        planetImage = planetImages.get(time);
        // Accounts for offset in the position of the text label to fit single digit and double-digit
        // times nicely.
        if (time == 0 || (10 <= time && time <= 12) || (22 <= time && time <= 23)) {
            timeLabel.setPosition(clockImage.getImageX() + 158f, clockImage.getImageY() + 139f);
        } else if ((1 <= time && time <= 9) || (13 <= time && time <= 21)) {
            timeLabel.setPosition(clockImage.getImageX() + 164f, clockImage.getImageY() + 139f);
        }
    }

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


    @Override
    public void dispose() {
        super.dispose();
        clockImage.remove();
        planetImage.remove();
        timeLabel.remove();
    }
}
