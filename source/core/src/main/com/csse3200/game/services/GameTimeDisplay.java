package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.TimeController;
import com.csse3200.game.services.ServiceLocator;

/**
 * A ui component for displaying the current game time on the Main Game Screen.
 */
public class GameTimeDisplay extends UIComponent {

    Table table;
    Group group;
    private Image clockImage;
    private Image planetImage;
    private Label timeLabel;
    private int time;

    private TimeController timeController;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        this.timeController = ServiceLocator.getTimeSource().getTimeController();
        this.timeController.setTimeDisplay(this);
    }

    /**
     * Sets the UI display for the time correctly.
     */
    private void addActors() {
        time = 0;
        update(time);
    }

    /**
     * Creates actors, which are the clock frame, planet image, and time label. Adds them
     *      * into a Group, and positions them with overlap. Adds this group into a table, then
     *      * adds the table to the stage to be written into creation.
     * @param time - the time to set display to.
     */
    public void update(int time) {
        table = new Table();
        group = new Group();
        table.top().left();
        table.setFillParent(true);
        table.padTop(150f).padLeft(-100f);

        clockImage = new Image(ServiceLocator.getResourceService().getAsset(
            "images/time_system_ui/clock_frame.png", Texture.class));
        planetImage = new Image(ServiceLocator.getResourceService().getAsset(
            String.format("images/time_system_ui/indicator_%d.png", time), Texture.class));

        // Set the correct text for the time label, dependent on time.
        if (time == 0) {
            timeLabel = new Label("12 am", skin, "large");
        } else if (time >= 1 && time < 12) {
            timeLabel = new Label(String.format("%d am", time), skin, "large");
        } else if (time == 12) {
            timeLabel = new Label("12 pm", skin, "large");
        } else {
            timeLabel = new Label(String.format("%d pm", time - 12), skin, "large");
        }

        // Accounts for offset in the position of the text label to fit single digit and double-digit
        // times nicely.
        if (time == 0 || (10 <= time && time <= 12) || (22 <= time && time <= 23)) {
            timeLabel.setPosition(clockImage.getImageX() + 158f, clockImage.getImageY() + 139f);
        } else if ((1 <= time && time <= 9) || (13 <= time && time <= 21)) {
            timeLabel.setPosition(clockImage.getImageX() + 164f, clockImage.getImageY() + 139f);
        }

        group.addActor(clockImage);
        group.addActor(planetImage);
        group.addActor(timeLabel);

        table.add(group).size(200);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage.
    }

    @Override
    public void dispose() {
        super.dispose();
        clockImage.remove();
        planetImage.remove();
        timeLabel.remove();
    }
}
