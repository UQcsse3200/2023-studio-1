package com.csse3200.game.components.missioninformation;

import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.screens.MainGameScreen;
import com.sun.nio.sctp.PeerAddressChangeNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MissionDisplay extends UIComponent {

    private Window window;
    private Window achWindow;
    private Window questWindow;
    private boolean isOpen;
    private boolean questsOpen = false;
    private boolean achOpen = false;
    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);

    @Override
    public void create() {
        super.create();
        addActors();
        isOpen = false;
        entity.getEvents().addListener("toggleMissions",this::toggleOpen);
        entity.getEvents().addListener("toggleQuests",this::toggleQuests);
        entity.getEvents().addListener("toggleAchievements",this::toggleAcheivements);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {

        // Create a window for the inventory using the skin
        window = new Window("Missions", skin);
        window.pad(40, 10, 10, 10); // Add padding to with so that the text doesn't go offscreen
        window.add("QUEST o ACHEI p  ");

        window.pack(); // Pack the window to the size
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
        window.setVisible(false);
        stage.addActor(window);

        achWindow = new Window("Achievements", skin);
        achWindow.pad(40, 10, 10, 10); // Add padding to with so that the text doesn't go offscreen
        achWindow.add("ACH:1\n");
        achWindow.pack(); // Pack the window to the size
        achWindow.setMovable(false);
        achWindow.setPosition(stage.getWidth() / 2 - achWindow.getWidth() / 2, stage.getHeight() / 2 - achWindow.getHeight() / 2); // Center the window on the stage
        achWindow.setVisible(false);
        stage.addActor(achWindow);

        questWindow = new Window("Quests", skin);
        questWindow.pad(40, 10, 10, 10); // Add padding to with so that the text doesn't go offscreen
        questWindow.add("QUEST:1");

        questWindow.pack(); // Pack the window to the size
        questWindow.setMovable(false);
        questWindow.setPosition(stage.getWidth() / 2 - questWindow.getWidth() / 2, stage.getHeight() / 2 - questWindow.getHeight() / 2); // Center the window on the stage
        questWindow.setVisible(false);
        stage.addActor(questWindow);
    }

    /**
     * Draw stage of inventory
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch)  {
    }

    public void toggleOpen(){
        if (isOpen) {
            window.setVisible(false);
            achWindow.setVisible(false);
            questWindow.setVisible(false);
            isOpen = false;
        } else {
            window.setVisible(true);
            isOpen = true;
        }
    }
    public void toggleQuests(){
        if (isOpen && achOpen) {
            window.setVisible(false);
            achWindow.setVisible(false);
            questWindow.setVisible(true);
            questsOpen = true;
            achOpen = false;
        } else if (isOpen) {
            window.setVisible(false);
            questWindow.setVisible(true);
            questsOpen = true;
            achOpen = false;
        }
    }
    public void toggleAcheivements(){
        if (isOpen && questsOpen) {
            window.setVisible(false);
            questWindow.setVisible(false);
            achWindow.setVisible(true);
            questsOpen = false;
            achOpen = true;
        } else if (isOpen) {
            window.setVisible(false);
            achWindow.setVisible(true);
            questsOpen = false;
            achOpen = true;
        }
    }
    @Override
    public void dispose() {
        super.dispose();
    }
}


