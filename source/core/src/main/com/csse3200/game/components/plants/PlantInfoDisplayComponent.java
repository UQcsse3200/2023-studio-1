package com.csse3200.game.components.plants;


import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class PlantInfoDisplayComponent extends UIComponent {

    /**
     * The window used to display the plant information.
     */
    private Window window;

    /**
     * The label for the text displaying the plant information.
     */
    private Label label;
    private  boolean madeFirstContact = false;
    private MissionManager missionManager;
    private boolean isOpen = true;

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        super.create();
        ServiceLocator.getPlantInfoService().getEvents().addListener("showPlantInfo", this::showPlantInfo);
        ServiceLocator.getPlantInfoService().getEvents().addListener("clearPlantInfo", this::clearInfo);
        ServiceLocator.getPlantInfoService().getEvents().addListener("toggleOpen", this::toggleOpen);
        ServiceLocator.getPlantInfoService().getEvents().addListener("madeFirstContact", this::madeFirstContact);
        window = new Window("", skin);
        createWindow("");
        makeFirstContactWindow();
        missionManager = ServiceLocator.getMissionManager();

    }

    private void madeFirstContact() {
        madeFirstContact = true;
    }

    private void toggleOpen(boolean isOpen) {
        this.isOpen = isOpen;
        window.clear();
        window.setVisible(isOpen);
        clearInfo();
    }

    /**
     * Create the window used to display the plant information.
     */
    private void createWindow(String windowName) {
        window.reset();
        window.getTitleLabel().setText(windowName);
        window.setVisible(isOpen);
        window.setSize(450f, 275f);
        window.padBottom(10f);
        window.setPosition(20f, 20f);
        window.setMovable(false);
        stage.addActor(window);
    }

    /**
     * Display the string of plant information in the window. This information is
     * provided by the plant component and is already formatted. This occurs when the
     * player hovers their mouse cursor over a plant.
     * @param plantInfo - Information about the current state of the plant.
     */
    public void showPlantInfo(String plantName, String plantInfo) {
        createWindow(plantName);
        label = new Label(plantInfo, skin);
        label.setFontScale(1.4f);
        label.setColor(Color.BROWN);
        window.add(label);
        stage.addActor(window);
    }

    /**
     * Clears the window of any plant information.
     */
    public void clearInfo() {
        if (madeFirstContact) {
            createWindow("Active Quests");
            List<Quest> quests = missionManager.getActiveQuests();

            String activeQuestsString = "";
            int numOfLines = 0; // will be used to configure that max amount of information in the window l8er
            if (!quests.isEmpty()) {
                for (Quest q : quests) {
                    if (q.isRewardCollected()) {
                        continue;
                    }

                    if (numOfLines == 0) {
                        activeQuestsString += q.getName();
                    } else {
                        activeQuestsString += "\n" + q.getName();
                    }
                    numOfLines += 1;

                }
            } else {
                activeQuestsString = "No Active Quests";
            }

            label = new Label(activeQuestsString, skin);
            label.setFontScale(1.4f);
            label.setColor(Color.BROWN);
            window.add(label);
            stage.addActor(window);
        } else {
            makeFirstContactWindow();
        }
    }

    private void makeFirstContactWindow() {
        window.reset();
        window.getTitleLabel().setText("First Contact");
        label = new Label("Better do what the angry\n Alien Creature says...", skin);
        label.setFontScale(1.4f);
        label.setColor(Color.BROWN);
        window.add(label);
        stage.addActor(window);
    }

    /**
     * Has no use in this component but is required by the UIComponent class.
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // No use but required.
    }

}
