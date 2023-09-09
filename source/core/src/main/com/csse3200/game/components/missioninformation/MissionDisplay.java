package com.csse3200.game.components.missioninformation;

import com.badlogic.gdx.graphics.Color;
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
    private boolean tamedAnimal;
    private int questNum;
    private  int plantsGrown;
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
        String tamedString;

        if (tamedAnimal){
            tamedString = " You have tamed an animal ";
        } else {
            tamedString = " You need to (tame method) to tame an animal ";
        }
        String questAchevimentString = " " + String.valueOf(questNum) + " out of 5 completed ";
        String plantsGrowString =" "  + String.valueOf(plantsGrown) + " out of 5 grown ";
        achWindow = new Window("Achievements", skin);

        achWindow.pad(40, 10, 10, 10);
        Table contentTable = new Table();
        contentTable.defaults().padBottom(10); // Set default padding for the content

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("pixel-body"); // Assuming you have a font named "pixel-body" in your skin file
        labelStyle.fontColor = Color.BLACK;

        Label header1Label = new Label(" Tame a wild animal ", skin, "pixel-mid", "black"); // Assuming you have a title font named "pixel-title"
        Label tamedLabel = new Label(tamedString, labelStyle);

        Label header2Label = new Label(" Complete 5 quests ", skin, "pixel-mid", "black"); // Assuming you have a title font named "pixel-title"
        Label questAchievementLabel = new Label(questAchevimentString, labelStyle);

        Label header3Label = new Label(" Grow 5 plants ", skin, "pixel-mid", "black"); // Assuming you have a title font named "pixel-title"
        Label thirdLabel = new Label(plantsGrowString, labelStyle);

        TextButton backButton = getBackButton();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                openMenu();
            }
        });

        contentTable.add(header1Label).left().row();
        contentTable.add(tamedLabel).left().row();
        contentTable.add(header2Label).left().row();
        contentTable.add(questAchievementLabel).left().row();
        contentTable.add(header3Label).left().row();
        contentTable.add(thirdLabel).left().row();
        contentTable.add(backButton).colspan(4).bottom().center().fill();

        achWindow.add(contentTable).expand().fill();

        achWindow.pack();
        achWindow.setMovable(false);
        achWindow.setPosition(stage.getWidth() / 2 - achWindow.getWidth() / 2, stage.getHeight() / 2 - achWindow.getHeight() / 2);
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
        contentTable.add(missionDescription).center().expand();
        contentTable.row();
        contentTable.add(buttonTable).center().expand();

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
     * Generates a Label containing the quest's name.
     *
     * @param quest to generate the label for
     * @return Label
     */
    private Label getQuestNameLabel(Quest quest) {
        return new Label(
                quest.getName(),
                skin,
                "pixel-mid",
                "black"
        );
    }

    /**
     * Generates a Label containing the quest's short description.
     *
     * @param quest to generate the label for
     * @return Label
     */
    private Label getQuestShortDescriptionLabel(Quest quest) {
        return new Label(
                quest.getShortDescription(),
                skin,
                "pixel-body",
                "black"
        );
    }

    /**
     * Generates a Label containing the quest's description.
     *
     * @param quest to generate the label for
     * @return Label
     */
    private Label getQuestDescriptionLabel(Quest quest) {
        Label descriptionLabel = new Label(
                quest.getDescription(),
                skin,
                "pixel-body",
                "black"
        );
        descriptionLabel.setWrap(true);
        return descriptionLabel;
    }

    /**
     * Generates a TextButton for a quest's action
     *
     * @param quest to generate the button for
     * @return TextButton
     */
    private TextButton getQuestActionButton(Quest quest) {
        return getQuestActionButton(quest, false);
    }

    /**
     * Generates a TextButton for a quest's action
     *
     * @param quest to generate the button for
     * @param isNew true if the quest hasn't been accepted yet
     * @return TextButton
     */
    private TextButton getQuestActionButton(Quest quest, boolean isNew) {
        TextButton actionButton = null;

        if (isNew) {
            actionButton = new TextButton(
                    "Accept",
                    skin,
                    "small"
            );
            actionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    missionManager.acceptQuest(quest);
                    generateQuestsMenu();
                }
            });
        } else if (quest.isExpired()) {
            actionButton = new TextButton(
                    "Reactivate",
                    skin,
                    "small"
            );
            actionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    quest.resetExpiry();
                    generateQuestsMenu();
                }
            });
        } else if (quest.isCompleted()) {
            actionButton = new TextButton(
                    "Collect Reward",
                    skin,
                    "small"
            );
            actionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    quest.collectReward();
                }
            });
        }

        return actionButton;
    }

    /**
     * Generates a TextButton to view a quest's info.
     *
     * @param quest to generate the button for
     * @return TextButton
     */
    private TextButton getQuestViewButton(Quest quest) {
        return getQuestViewButton(quest, false);
    }

    /**
     * Generates a TextButton to view a quest's info.
     *
     * @param quest to generate the button for
     * @param isNew true if the quest hasn't been accepted yet
     * @return TextButton
     */
    private TextButton getQuestViewButton(Quest quest, boolean isNew) {
        TextButton viewButton = new TextButton(
                "View",
                skin,
                "small"
        );
        viewButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                generateQuestInfo(quest, isNew);
            }
        });

        return viewButton;
    }

    private TextButton getBackButton() {
        return new TextButton(
                "< Back",
                skin,
                "small"
        );
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

        if (!selectableQuests.isEmpty()) {
            questTable.row();
            questTable.add(newQuestsLabel).colspan(4);

            for (Quest quest : selectableQuests) {
                questTable.row();
                questTable.add(getQuestNameLabel(quest)).right().pad(10f);
                questTable.add(getQuestShortDescriptionLabel(quest)).pad(10f);
                questTable.add(getQuestActionButton(quest, true)).pad(10f).fill();
                questTable.add(getQuestViewButton(quest, true)).pad(10f).fill();
            }
        }

        if (!inProgressQuests.isEmpty()) {
            questTable.row();
            questTable.add(activeQuestsLabel).colspan(4);

            for (Quest quest : inProgressQuests) {
                questTable.row();
                questTable.add(getQuestNameLabel(quest)).right().pad(10f);
                questTable.add(getQuestShortDescriptionLabel(quest)).pad(10f);
                questTable.add().pad(10f);
                questTable.add(getQuestViewButton(quest)).pad(10f).fill();
            }
        }

        if (!expiredQuests.isEmpty()) {
            questTable.row();
            questTable.add(expiredQuestsLabel).colspan(4);

            for (Quest quest : expiredQuests) {
                questTable.row();
                questTable.add(getQuestNameLabel(quest)).right().pad(10f);
                questTable.add(getQuestShortDescriptionLabel(quest)).pad(10f);
                questTable.add(getQuestActionButton(quest)).pad(10f).fill();
                questTable.add(getQuestViewButton(quest)).pad(10f).fill();
            }
        }

        if (!completedQuests.isEmpty()) {
            questTable.row();
            questTable.add(completedQuestsLabel).colspan(4);

            for (Quest quest : completedQuests) {
                questTable.row();
                questTable.add(getQuestNameLabel(quest)).right().pad(10f);
                questTable.add(getQuestShortDescriptionLabel(quest)).pad(10f);
                questTable.add(getQuestActionButton(quest)).pad(10f).fill();
                questTable.add(getQuestViewButton(quest)).pad(10f).fill();
            }
        }


        TextButton backButton = getBackButton();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                openMenu();
            }
        });

        questTable.row().pad(30f);
        questTable.add(backButton).colspan(4).center().fill();

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
     * Generates an information page for a quest.
     *
     * @param quest to generate the page for
     * @param isNew true if the quest hasn't been accepted yet
     */
    private void generateQuestInfo(Quest quest, boolean isNew) {
        questWindow.clear();

        TextButton backButton = getBackButton();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                generateQuestsMenu();
            }
        });

        TextButton actionButton = getQuestActionButton(quest, isNew);

        Table questInfoTable = new Table();

        questInfoTable.defaults().size(500f, 50f);
        questInfoTable.row().pad(30f);
        questInfoTable.add(getQuestNameLabel(quest)).fill();
        questInfoTable.row().pad(30f);
        questInfoTable.add(getQuestDescriptionLabel(quest)).center().colspan(2).fill();

        if (actionButton != null) {
            questInfoTable.row().padTop(30f);
            questInfoTable.add(actionButton).center();
        }

        questInfoTable.row().pad(30f);
        questInfoTable.add(backButton).fill();

        questWindow.add(questInfoTable);
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
            openMenu();
        }
    }

    /**
     * Sets the main mission menu to visible.
     */
    public void openMenu() {
        generateMissionMenu();

        window.setVisible(true);
        achWindow.setVisible(false);
        questWindow.setVisible(false);

        isOpen = true;
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


