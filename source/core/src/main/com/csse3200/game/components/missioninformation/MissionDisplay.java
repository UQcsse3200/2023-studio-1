package com.csse3200.game.components.missioninformation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Renders a UI for interacting with Missions.
 */
public class MissionDisplay extends UIComponent {
    private MissionManager missionManager;

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
        missionManager = ServiceLocator.getMissionManager();
        isOpen = false;

        addActors();

        entity.getEvents().addListener("toggleMissions", this::toggleOpen);
        entity.getEvents().addListener("toggleAchievements", this::toggleAchievements);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private void addActors() {
        window = new Window("Mission Giver", skin);
        window.setVisible(false);
        stage.addActor(window);

        questWindow = new Window("Quests", skin);
        questWindow.setVisible(false);
        stage.addActor(questWindow);

        achWindow = new Window("Achievements", skin);
        achWindow.pad(40, 10, 10, 10); // Add padding to with so that the text doesn't go offscreen
        achWindow.add("ACH:1\n");
        achWindow.pack(); // Pack the window to the size
        achWindow.setMovable(false);
        achWindow.setPosition(stage.getWidth() / 2 - achWindow.getWidth() / 2, stage.getHeight() / 2 - achWindow.getHeight() / 2); // Center the window on the stage
        achWindow.setVisible(false);
        stage.addActor(achWindow);
    }

    /**
     * Generates the main menu for missions. Contains a small blurb from the Mission NPC
     * and allows the user to view either quests or achievements.
     */
    private void generateMissionMenu() {
        window.clear();

        TextButton achievementsButton = new TextButton("Achievements", skin);
        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Achievements button clicked");
                toggleAchievements();
            }
        });


        TextButton questsButton = new TextButton("Quests", skin);
        questsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Quests button clicked");
                openQuests();
            }
        });

        Table buttonTable = new Table();
        buttonTable.row();
        buttonTable.add(achievementsButton).pad(30f);
        buttonTable.add(questsButton).pad(30f);

        Label missionDescription = new Label(
                "Looks like your skills could be useful for us here on Alpha Centauri. Why don't you take a look at how you can help?",
                skin,
                "pixel-body",
                "black"
        );
        missionDescription.setWrap(true);
        missionDescription.setAlignment(Align.center);

        Table contentTable = new Table();
        contentTable.defaults().size(500f, 50f);
        contentTable.row().padBottom(30f).padTop(30f);
        contentTable.add(missionDescription).center().expandX().expandY();
        contentTable.row();
        contentTable.add(buttonTable).center().expandX().expandY();

        window.add(contentTable);
        window.pad(50, 50, 50, 50);
        window.pack();
        window.setMovable(false);
        window.setPosition(
                stage.getWidth() / 2 - window.getWidth() / 2,
                stage.getHeight() / 2 - window.getHeight() / 2
        ); // center on stage

    }

    /**
     * Generates the quest menu. Contains a table split into new, in progress, expired and completed quests.
     */
    private void generateQuestsMenu() {
        questWindow.clear();

        List<Quest> selectableQuests = missionManager.getSelectableQuests();
        List<Quest> activeQuests = missionManager.getActiveQuests();

        List<Quest> inProgressQuests = activeQuests.stream().filter(quest -> !(quest.isExpired() || quest.isCompleted())).toList();
        List<Quest> expiredQuests = activeQuests.stream().filter(Quest::isExpired).toList();
        List<Quest> completedQuests = activeQuests.stream().filter(Quest::isCompleted).toList();

        Label newQuestsLabel = new Label(
                "New",
                skin,
                "pixel-mid",
                "black"
        );
        Label activeQuestsLabel = new Label(
                "Active",
                skin,
                "pixel-mid",
                "black"
        );
        Label expiredQuestsLabel = new Label(
                "Expired",
                skin,
                "pixel-mid",
                "black"
        );
        Label completedQuestsLabel = new Label(
                "Completed",
                skin,
                "pixel-mid",
                "black"
        );

        Table questTable = new Table();

        questTable.row();
        questTable.add(newQuestsLabel).colspan(4);

        for (Quest quest : selectableQuests) {
            questTable.row();

            Label questName = new Label(
                    quest.getName(),
                    skin,
                    "pixel-mid",
                    "black"
            );
            Label questDescription = new Label(
                    quest.getShortDescription(),
                    skin,
                    "pixel-body",
                    "black"
            );

            TextButton button = new TextButton(
                    "Accept",
                    skin,
                    "small"
            );
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    missionManager.acceptQuest(quest);
                    generateQuestsMenu();
                }
            });
            TextButton viewButton = new TextButton(
                    "View",
                    skin,
                    "small"
            );

            questTable.add(questName).right().pad(10f);
            questTable.add(questDescription).pad(10f);
            questTable.add(button).pad(10f);
            questTable.add(viewButton).pad(10f);
        }

        questTable.row();
        questTable.add(activeQuestsLabel).colspan(4);

        for (Quest quest : inProgressQuests) {
            Label questName = new Label(
                    quest.getName(),
                    skin,
                    "pixel-mid",
                    "black"
            );
            Label questDescription = new Label(
                    quest.getShortDescription(),
                    skin,
                    "pixel-body",
                    "black"
            );

            TextButton viewButton = new TextButton(
                    "View",
                    skin,
                    "small"
            );

            questTable.row();
            questTable.add(questName).right().pad(10f);
            questTable.add(questDescription).pad(10f);
            questTable.add();
            questTable.add(viewButton).pad(10f);
        }

        questTable.row();
        questTable.add(expiredQuestsLabel).colspan(4);

        for (Quest quest : expiredQuests) {
            Label questName = new Label(
                    quest.getName(),
                    skin,
                    "pixel-mid",
                    "black"
            );
            Label questDescription = new Label(
                    quest.getShortDescription(),
                    skin,
                    "pixel-body",
                    "black"
            );

            questTable.row();
            questTable.add(questName).right().pad(10f);
            questTable.add(questDescription).pad(10f);

            TextButton button = new TextButton(
                    "Reactivate",
                    skin,
                    "small"
            );
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    quest.resetExpiry();
                    generateQuestsMenu();
                }
            });

            questTable.add(button).pad(10f);

            TextButton viewButton = new TextButton(
                    "View",
                    skin,
                    "small"
            );

            questTable.add(viewButton).pad(10f);
        }

        questTable.row();
        questTable.add(completedQuestsLabel).colspan(4);

        for (Quest quest : completedQuests) {
            Label questName = new Label(
                    quest.getName(),
                    skin,
                    "pixel-mid",
                    "black"
            );
            Label questDescription = new Label(
                    quest.getShortDescription(),
                    skin,
                    "pixel-body",
                    "black"
            );

            questTable.row();
            questTable.add(questName).right().pad(10f);
            questTable.add(questDescription).pad(10f);

            TextButton button = new TextButton(
                    "Collect Reward",
                    skin,
                    "small"
            );
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    quest.collectReward();
                }
            });

            questTable.add(button).pad(10f);

            TextButton viewButton = new TextButton(
                    "View",
                    skin,
                    "small"
            );

            questTable.add(viewButton).pad(10f);
        }

        questWindow.add(questTable);
        questWindow.pad(50, 50, 50, 50);
        questWindow.pack();
        questWindow.setMovable(false);
        questWindow.setPosition(
                stage.getWidth() / 2 - questWindow.getWidth() / 2,
                stage.getHeight() / 2 - questWindow.getHeight() / 2
        ); // center on stage
    }

    /**
     * Draw stage, unused for now.
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
    }

    /**
     * Toggles the visibility of the main mission ui.
     */
    public void toggleOpen() {
        if (isOpen) {
            window.setVisible(false);
            achWindow.setVisible(false);
            questWindow.setVisible(false);

            isOpen = false;
        } else {
            generateMissionMenu();

            window.setVisible(true);
            isOpen = true;
        }
    }

    /**
     * Opens the quest menu.
     */
    public void openQuests() {
        if (isOpen) {
            generateQuestsMenu();

            questWindow.setVisible(true);
            window.setVisible(false);
            achWindow.setVisible(false);

            questsOpen = true;
            achOpen = false;
        }
    }

    /**
     * Opens the achievement menu.
     */
    public void toggleAchievements() {
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

    /**
     * Cleans up.
     */
    @Override
    public void dispose() {
        window.clear();
        questWindow.clear();
        achWindow.clear();

        super.dispose();
    }
}


