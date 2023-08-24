package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying the current game time.
 */
public class GameTimeDisplay extends UIComponent{

    Table table;
    Stack stack;
    Group group;
    private Image clockImage;
    private Image planetImage;
    private Label timeLabel;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        stack = new Stack();
        group = new Group();
        table.top().left();
        table.setFillParent(true);
        table.padTop(220f).padLeft(-100f);

        clockImage = new Image(ServiceLocator.getResourceService().getAsset(
            "images/time_system_ui/clock_frame.png", Texture.class));
        planetImage = new Image(ServiceLocator.getResourceService().getAsset(
            "images/time_system_ui/indicator_12.png", Texture.class));

        timeLabel = new Label("12 am", skin, "large");


        group.addActor(clockImage);
        group.addActor(planetImage);
        timeLabel.setPosition(clockImage.getImageX() + 156f, clockImage.getImageY() + 140f);
        group.addActor(timeLabel);

        table.add(group).size(200);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Need to implement.
    }

    /**
     * Updates the time on the ui.
     */
    public void updateTimeDisplay() {
        // Need to implement.
    }

    @Override
    public void dispose() {
        super.dispose();
        clockImage.remove();
        planetImage.remove();
        timeLabel.remove();
    }

}
