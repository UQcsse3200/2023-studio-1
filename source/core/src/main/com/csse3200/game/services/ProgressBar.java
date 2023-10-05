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
    private Image progressionbar;
    private Array<Image> progressBarImagesAct1;
    private Array<Image> progressBarImagesAct2;
    private Array<Image> progressBarImagesAct3;
    private int act;
    private int dayOffset;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        this.act = 1;
        this.dayOffset = 0;
        ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::updateDisplay);
        ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::updateDisplay);
        ServiceLocator.getUIService().getEvents().addListener("toggleUI", this::toggleDisplay);
        ServiceLocator.getMissionManager().getEvents().addListener("An Agreement", this::updateProgressBarAct2);
        ServiceLocator.getMissionManager().getEvents().addListener("Making Contact", this::updateProgressBarAct3);
        progressBarImagesAct1 = new Array<Image>();
        progressBarImagesAct2 = new Array<Image>();
        progressBarImagesAct3 = new Array<Image>();
        updateDisplay();
    }
    private void toggleDisplay(boolean isDisplayed) {
        table.setVisible(isDisplayed);
    }
    /**
     * Creates each image as an image object then adds to an array of images for later use
     */
    public void createTexture() {
        for (int i = 1; i <= 30; i++) {
            if (i <=5 ) {
                progressBarImagesAct1.add(new Image(ServiceLocator.getResourceService().getAsset(
                        String.format("images/progress-bar/part1day%d.png", i), Texture.class)));
            } else if (i <= 15) {
                progressBarImagesAct2.add(new Image(ServiceLocator.getResourceService().getAsset(
                        String.format("images/progress-bar/part2day%d.png", i-5), Texture.class)));
            } else {
                progressBarImagesAct3.add(new Image(ServiceLocator.getResourceService().getAsset(
                        String.format("images/progress-bar/part3day%d.png", i-15), Texture.class)));
            }
        }
    }

    public void updateProgressBarAct2() {
        this.act = 2;
        this.dayOffset = ServiceLocator.getTimeService().getDay();
    }

    public void updateProgressBarAct3() {
        this.act = 3;
        this.dayOffset = ServiceLocator.getTimeService().getDay();
    }

    /**
     * Updates the display dependent on what day it is.
     * Will also call createTexture(), if the array is empty.
     */
    public void updateDisplay() {
        if (progressBarImagesAct1.isEmpty()) {
            createTexture();
        }
        int day = ServiceLocator.getTimeService().getDay() - dayOffset;

        switch (this.act) {
            case 1 -> progressionbar = (day > 5) ? progressBarImagesAct1.get(0) : progressBarImagesAct1.get(day);
            case 2 -> progressionbar = (day > 10) ? progressBarImagesAct2.get(0) : progressBarImagesAct2.get(day);
            case 3 -> progressionbar = (day > 15) ? progressBarImagesAct3.get(0) : progressBarImagesAct3.get(day);
        }
    }

    /**
     * Draws the actors to the game.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        table.clear();
        group.clear();
        table.top().left();
        table.setFillParent(true);
        table.padTop(-30f).padLeft(190f);


        group.addActor(progressionbar);

        table.add(group).size(200);
        stage.addActor(table);
    }


    /**
     * Destroys the UI objects
     */
    @Override
    public void dispose() {
        super.dispose();
        progressionbar.remove();
    }
}
