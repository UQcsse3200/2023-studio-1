package com.csse3200.game.components.questgiver;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.missions.achievements.Achievement;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Renders a UI for interacting with Missions.
 */
public class MissionDisplay extends UIComponent {
	private Window window;
	private boolean isOpen;
	private boolean showCompletedMissions = false;

	private static final String TEXT_COLOUR = "black";
	private static final String PIXEL_BODY = "pixel-body";
	private static final String BACKGROUND_COLOUR = "small-grey";
	private static final String SIZE = "small";

	@Override
	public void create() {
		super.create();
		isOpen = false;

		addActors();

		entity.getEvents().addListener("interact", this::toggleOpen);
	}

	/**
	 * Creates a window for the missions and adds it to the stage.
	 */
	private void addActors() {
		window = new Window("Mission Giver", skin);
		window.setVisible(false);
		stage.addActor(window);
	}

	/**
	 * Recalculates the window height and centers it on the screen after an update.
	 */
	private void updateWindow() {
		window.setMovable(false);
		window.pad(50, 10, 10, 10);
		window.pack();
		window.setWidth(800f);
		window.setPosition(
				stage.getWidth() / 2 - window.getWidth() / 2,
				stage.getHeight() / 2 - window.getHeight() / 2
		); // center on stage
	}

	/**
	 * Generates a table of achievements containing their names & descriptions.
	 *
	 * @param achievementsTable parent table to add content to
	 * @param achievements      list of achievements to include
	 */
	private void createAchievementsTable(Table achievementsTable, List<Achievement> achievements) {
		achievementsTable.clearChildren();
		for (Achievement achievement : achievements) {
			Label titleLabel = new Label(achievement.getName(), skin, "pixel-mid", TEXT_COLOUR);
			Label descriptionLabel = new Label(achievement.getDescription(), skin, PIXEL_BODY, TEXT_COLOUR);

			achievementsTable.add(titleLabel).left().expand().fill().row();
			achievementsTable.add(descriptionLabel).left().expand().fill().padBottom(10f).row();
		}
	}

	/**
	 * Generates the achievements menu with a toggle between in progress & completed achievements.
	 */
	private void generateAchievements() {
		window.clear();
		window.getTitleLabel().setText("Incomplete Achievements");

		Achievement[] achievements = ServiceLocator.getMissionManager().getAchievements();

		Table achievementsTable = new Table();

		Table contentTable = new Table();
		contentTable.defaults().padBottom(10);

		TextButton backButton = getBackButton();
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				openMenu();
			}
		});

		TextButton completeButton = new TextButton(
				"Complete",
				skin,
				BACKGROUND_COLOUR
		);
		TextButton incompleteButton = new TextButton(
				"Incomplete",
				skin,
				BACKGROUND_COLOUR
		);

		completeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				showCompletedMissions = true;
				// Clear the current achievement table and rebuild it based on the toggle
				createAchievementsTable(
						achievementsTable,
						Arrays.stream(achievements).filter(achievement -> showCompletedMissions == achievement.isCompleted()).toList()
				);

				window.getTitleLabel().setText("Complete Achievements");
				updateWindow();
			}
		});

		incompleteButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				showCompletedMissions = false;
				// Clear the current achievement table and rebuild it based on the toggle
				createAchievementsTable(
						achievementsTable,
						Arrays.stream(achievements).filter(achievement -> showCompletedMissions == achievement.isCompleted()).toList()
				);

				window.getTitleLabel().setText("Incomplete Achievements");
				updateWindow();
			}
		});

		Table tabs = new Table();
		tabs.add(incompleteButton).expand().fill();
		tabs.add(completeButton).expand().fill();

		// Populate the tables based on the initial state
		createAchievementsTable(
				achievementsTable,
				Arrays.stream(achievements).filter(achievement -> showCompletedMissions == achievement.isCompleted()).toList()
		);


		contentTable.add(tabs).colspan(2).bottom().center().fill();
		contentTable.row();
		contentTable.add(achievementsTable).expand().fill().pad(10f);
		contentTable.row();
		contentTable.add(backButton).colspan(2).bottom().center().fill();

		window.add(contentTable).expand().fill();
		updateWindow();
	}

	/**
	 * Generates the main menu for missions. Contains a small blurb from the Mission NPC
	 * and allows the user to view either quests or achievements.
	 */
	private void generateMissionMenu() {
		window.clear();
		window.getTitleLabel().setText("Mission Giver");

		TextButton achievementsButton = new TextButton("Achievements", skin);
		achievementsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				openAchievements();
			}
		});


		TextButton questsButton = new TextButton("Quests", skin);
		questsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				openQuests();
			}
		});


		/* CLOSE MENU BUTTON QUICK FIX */
		TextButton closeButton = new TextButton("Close", skin);
		closeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				toggleOpen();
			}
		});


		Table buttonTable = new Table();
		buttonTable.row();
		buttonTable.add(achievementsButton).pad(30f);
		buttonTable.add(questsButton).pad(30f);
		buttonTable.add(closeButton).pad(30f); // QUICK FIX

		Label missionDescription = new Label(
				"Looks like your skills could be useful for us here on Alpha Centauri. Why don't you take a look at how you can help?",
				skin,
				PIXEL_BODY,
				TEXT_COLOUR
		);
		missionDescription.setWrap(true);
		missionDescription.setAlignment(Align.center);

		Table contentTable = new Table();

		contentTable.defaults().size(600f, 50f);
		contentTable.row().padBottom(30f).padTop(30f);
		contentTable.add(missionDescription).center().expand();
		contentTable.row();
		contentTable.add(buttonTable).center().expand();

		window.add(contentTable);
		updateWindow();
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
				TEXT_COLOUR
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
				PIXEL_BODY,
				TEXT_COLOUR
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
				PIXEL_BODY,
				TEXT_COLOUR
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
					SIZE
			);
			actionButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					ServiceLocator.getMissionManager().acceptQuest(quest);
					generateQuestsMenu();
				}
			});
		} else if (quest.isExpired()) {
			actionButton = new TextButton(
					"Reactivate",
					skin,
					SIZE
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
					SIZE
			);
			actionButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					quest.collectReward();
					ServiceLocator.getGameArea().getPlayer().getEvents().trigger("startEffect", ParticleService.ParticleEffectType.SUCCESS_EFFECT);
					generateQuestsMenu();
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
				SIZE
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
				SIZE
		);
	}

	/**
	 * Creates a table layout with a quest's name & short description.
	 *
	 * @param quest to get info about
	 * @param width to set for the info
	 * @return Table containing the name & short description
	 */
	private Table getQuestShortInfo(Quest quest, float width) {
		Table content = new Table();
		Label questName = getQuestNameLabel(quest);
		Label questDescription = getQuestShortDescriptionLabel(quest);

		content.add(questName).width(width);
		content.row();
		content.add(questDescription).width(width);

		return content;
	}

	/**
	 * Generates a table of quest info & buttons.
	 *
	 * @param questTable      a Table to put the quest info in
	 * @param quests          a list of Quests to put in the table
	 * @param getActionButton a function to get an action button for the passed in quests
	 * @param getViewButton   a function to get the view button for the passed in quests
	 */
	private void generateQuestTable(
			Table questTable,
			List<Quest> quests,
			Function<Quest, TextButton> getActionButton,
			Function<Quest, TextButton> getViewButton
	) {
		questTable.clear();

		for (Quest quest : quests) {
			TextButton actionButton = getActionButton.apply(quest);

			questTable.row();
			questTable.add(getQuestShortInfo(quest, actionButton != null ? 400f : 600f)).width(actionButton != null ? 400f : 600f).pad(10f);
			questTable.add(getActionButton.apply(quest)).width(actionButton != null ? 200f : 0f).pad(10f).fillX();
			questTable.add(getViewButton.apply(quest)).width(100f).pad(10f).fillX();
		}

	}

	/**
	 * Generates the quest menu. Contains a table split into new, in progress, expired and completed quests.
	 */
	private void generateQuestsMenu() {
		window.clear();
		window.getTitleLabel().setText("Active Quests");

		List<Quest> selectableQuests = ServiceLocator.getMissionManager().getSelectableQuests();
		List<Quest> activeQuests = ServiceLocator.getMissionManager().getActiveQuests();
		List<Quest> inProgressQuests = activeQuests.stream().filter(quest -> !(quest.isExpired() || quest.isCompleted())).toList();
		List<Quest> expiredQuests = activeQuests.stream().filter(Quest::isExpired).toList();
		List<Quest> completedQuests = activeQuests.stream().filter(quest -> quest.isCompleted() && !quest.isRewardCollected()).toList();

		TextButton newButton = new TextButton(
				"New",
				skin,
				BACKGROUND_COLOUR
		);
		TextButton activeButton = new TextButton(
				"Active",
				skin,
				BACKGROUND_COLOUR
		);
		TextButton expiredButton = new TextButton(
				"Expired",
				skin,
				BACKGROUND_COLOUR
		);
		TextButton completedButton = new TextButton(
				"Completed",
				skin,
				BACKGROUND_COLOUR
		);


		TextButton backButton = getBackButton();
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				openMenu();
			}
		});

		Table questTable = new Table();
		generateQuestTable(
				questTable,
				inProgressQuests,
				quest -> null,
				this::getQuestViewButton
		);

		// tab buttons will regenerate the table with their respective quests.
		newButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				generateQuestTable(
						questTable,
						selectableQuests,
						quest -> getQuestActionButton(quest, true),
						quest -> getQuestViewButton(quest, true)
				);

				window.getTitleLabel().setText("New Quests");
				updateWindow();
			}
		});

		activeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				generateQuestTable(
						questTable,
						inProgressQuests,
						quest -> null,
						quest -> getQuestViewButton(quest)
				);

				window.getTitleLabel().setText("Active Quests");
				updateWindow();
			}
		});

		expiredButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				generateQuestTable(
						questTable,
						expiredQuests,
						quest -> getQuestActionButton(quest),
						quest -> getQuestViewButton(quest)
				);

				window.getTitleLabel().setText("Expired Quests");
				updateWindow();
			}
		});

		completedButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				generateQuestTable(
						questTable,
						completedQuests,
						quest -> getQuestActionButton(quest),
						quest -> getQuestViewButton(quest)
				);

				window.getTitleLabel().setText("Completed Quests");
				updateWindow();
			}
		});

		Table tabs = new Table();
		tabs.add(newButton).expand().fill();
		tabs.add(activeButton).expand().fill();
		tabs.add(expiredButton).expand().fill();
		tabs.add(completedButton).expand().fill();

		tabs.pad(10f);
		questTable.pad(10f);
		backButton.pad(10f);

		Table content = new Table();

		content.add(tabs).expand().fill();
		content.row();
		content.add(questTable).expand().fill();
		content.row();
		content.add(backButton).expand().fill();

		window.add(content).expand().fill();
		updateWindow();
	}

	/**
	 * Generates an information page for a quest.
	 *
	 * @param quest to generate the page for
	 * @param isNew true if the quest hasn't been accepted yet
	 */
	private void generateQuestInfo(Quest quest, boolean isNew) {
		window.clear();
		window.getTitleLabel().setText("Quest Info");

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

		window.add(questInfoTable);
		updateWindow();
	}

	/**
	 * Draw stage, unused for now.
	 *
	 * @param batch Batch to render to.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		// Handled else where
	}

    /**
     * Toggles the visibility of the main mission ui.
     */
    public void toggleOpen() {
        ServiceLocator.getPlantInfoService().getEvents().trigger("madeFirstContact");
        ServiceLocator.getPlantInfoService().getEvents().trigger("clearPlantInfo");

        if (isOpen) {
            window.setVisible(false);
            isOpen = false;
            ServiceLocator.getGameArea().getPlayer().getEvents().trigger(PlayerActions.events.UNFREEZE.name());
        } else {
            // Prevent player from moving - freeze player
            ServiceLocator.getGameArea().getPlayer().getEvents().trigger(PlayerActions.events.FREEZE.name());
            openMenu();
        }
    }

	/**
	 * Sets the main mission menu to visible.
	 */
	public void openMenu() {
		generateMissionMenu();

		window.setVisible(true);

		isOpen = true;
	}

	/**
	 * Opens the quest menu.
	 */
	public void openQuests() {
		if (isOpen) {
			generateQuestsMenu();
		}
	}

	/**
	 * Opens the achievement menu.
	 */
	public void openAchievements() {
		if (isOpen) {
			generateAchievements();
		}
	}

	/**
	 * Cleans up.
	 */
	@Override
	public void dispose() {
		window.clear();

		super.dispose();
	}
}


