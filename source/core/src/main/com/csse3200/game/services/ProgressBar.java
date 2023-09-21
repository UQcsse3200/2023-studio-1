package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;

public class ProgressBar extends UIComponent {

    Table table = new Table();
    Group group = new Group();
    private Image progressBar;
    private Array<Image> progressBarImages;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::updateDisplay);
        progressBarImages = new Array<Image>();
        updateDisplay();
    }

    public void createTexture() {
        for (int i = 1; i <= 2; i++) {
            progressBarImages.add(new Image(ServiceLocator.getResourceService().getAsset(
                    String.format("images/progress-bar/day%d.png", i), Texture.class)));
        }
    }

    public void updateDisplay() {
        if (progressBarImages.isEmpty()) {
            createTexture();
        }
        int day = ServiceLocator.getTimeService().getDay();
        progressBar = progressBarImages.get(day);
    }

    @Override
    public void draw(SpriteBatch batch) {
        table.clear();
        group.clear();
        table.top().center();
        table.setFillParent(true);
        table.padTop(150f);


        group.addActor(progressBar);

        table.add(group).size(200);
        stage.addActor(table);
    }


    @Override
    public void dispose() {
        super.dispose();
        progressBar.remove();
    }
}
