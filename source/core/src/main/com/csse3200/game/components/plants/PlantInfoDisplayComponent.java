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

/**
 * A UI component for displaying information about a plant.
 */
public class PlantInfoDisplayComponent extends UIComponent {

    /**
     * The window used to display the plant information.
     */
    private Window window;
    /**
     * The label for the text displaying the plant information.
     */
    private Label label;
    /**
     * Indicates whether the first contact has been made with a plant.
     */
    private boolean madeFirstContact = false;
    /**
     * The manager responsible for handling game missions and quests.
     */
    private MissionManager missionManager;
    /**
     * Indicates whether the plant information window is open or closed.
     */
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

    /**
     * Marks that the first contact has been made with the plant.
     */
    public void madeFirstContact() {
        this.madeFirstContact = true;
    }

    /**
     * Whether first contact has been made or not
     * @return True if first contact has been made, false otherwise
     */
    public boolean getMadeFirstContact() {
        return this.madeFirstContact;
    }

    /**
     * Toggles the visibility of the plant information window.
     * @param isOpen The new visibility status of the window.
     */
    private void toggleOpen(boolean isOpen) {
        this.isOpen = isOpen;
        window.clear();
        window.setVisible(isOpen);
        clearInfo();
    }

    /**
     * Create the window used to display the plant information.
     * @param windowName The name to display in the window's title.
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
     * @param plantName  The name of the plant.
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
        if (getMadeFirstContact()) {
            createWindow("Active Quests");
            List<Quest> quests = missionManager.getActiveQuests();

            StringBuilder activeQuestsString = new StringBuilder("No Active Quests");
            int numOfLines = 0; // will be used to configure that max amount of information in the window later
            for (Quest q : quests) {
                if (q.isRewardCollected()) {
                    continue;
                }

                if (numOfLines == 0) {
                    activeQuestsString = new StringBuilder(q.getName());
                } else {
                    activeQuestsString.append("\n").append(q.getName());
                }
                numOfLines += 1;

            }

            label = new Label(activeQuestsString.toString(), skin);
            label.setFontScale(1.4f);
            label.setColor(Color.BROWN);
            window.add(label);
            stage.addActor(window);
        } else {
            makeFirstContactWindow();
        }
    }

    /**
     * Create and display the window for first contact with the plant.
     */
    public void makeFirstContactWindow() {
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
