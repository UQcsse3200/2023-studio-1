package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.QuestFactory;
import com.csse3200.game.ui.UIComponent;

import java.util.Objects;

public class ProgressBar extends UIComponent {
    Table table = new Table();
    Group group = new Group();
    private Image progressionBar;
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
        ServiceLocator.getMissionManager().getEvents().addListener(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), this::updateProgressBar);
        progressBarImagesAct1 = new Array<>();
        progressBarImagesAct2 = new Array<>();
        progressBarImagesAct3 = new Array<>();
        updateDisplay();
    }
    private void toggleDisplay(boolean isDisplayed) {
        table.setVisible(isDisplayed);
    }
    /**
     * Creates each image as an image object then adds to an array of images for later use
     */
    public void createTexture() {
        for (int i = 1; i <= 18; i++) {
            if (i <=3 ) {
                progressBarImagesAct1.add(new Image(ServiceLocator.getResourceService().getAsset(
                        String.format("images/progress-bar/part1day%d.png", i), Texture.class)));
            } else if (i <= 9) {
                progressBarImagesAct2.add(new Image(ServiceLocator.getResourceService().getAsset(
                        String.format("images/progress-bar/part2day%d.png", i-3), Texture.class)));
            } else {
                progressBarImagesAct3.add(new Image(ServiceLocator.getResourceService().getAsset(
                        String.format("images/progress-bar/part3day%d.png", i-9), Texture.class)));
            }
        }
    }

    /**
     * Updates which array the images are pulled from based on the progress of the quests
     */
    public void updateProgressBar(String questName) {
        if (Objects.equals(questName, QuestFactory.FIRST_CONTACT_QUEST_NAME)) {
            updateProgressBarAct1();
        } else if (Objects.equals(questName, QuestFactory.CONNECTION_QUEST_NAME)) {
            updateProgressBarAct2();
        } else if (Objects.equals(questName, QuestFactory.AN_IMMINENT_THREAT_QUEST_NAME)) {
            updateProgressBarAct3();
        }
    }

    /**
     * Updates which act is currently and corrects the day count.
     */
    private void updateProgressBarAct1() {
        this.act = 1;
        this.dayOffset = ServiceLocator.getTimeService().getDay();
        this.updateDisplay();
    }

    /**
     * Updates which act is currently and corrects the day count.
     */
    private void updateProgressBarAct2() {
        this.act = 2;
        this.dayOffset = ServiceLocator.getTimeService().getDay();
        this.updateDisplay();
    }

    /**
     * Updates which act is currently and corrects the day count.
     */
    private void updateProgressBarAct3() {
        this.act = 3;
        this.dayOffset = ServiceLocator.getTimeService().getDay();
        this.updateDisplay();
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

            case 1 -> progressionBar = (day >= 3) ? progressBarImagesAct1.get(2) : progressBarImagesAct1.get(day);
            case 2 -> progressionBar = (day >= 6) ? progressBarImagesAct2.get(5) : progressBarImagesAct2.get(day);
            case 3 -> progressionBar = (day >= 9) ? progressBarImagesAct3.get(8) : progressBarImagesAct3.get(day);
            default -> {
                // Default case should do nothing
            }
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
        table.padTop(-95f).padLeft(180f);

        group.addActor(progressionBar);
        table.add(group).size(200);
        stage.addActor(table);
    }


    /**
     * Destroys the UI objects
     */
    @Override
    public void dispose() {
        super.dispose();
        progressionBar.remove();
    }
}
