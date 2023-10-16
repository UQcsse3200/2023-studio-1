package com.csse3200.game.components.missiondisplay;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.questgiver.MissionDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.FertiliseCropTilesQuest;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class MissionDisplayTest {
	Stage stage;
	MissionManager missionManager;
	MissionDisplay missionDisplay;
	ArgumentCaptor<Actor> actorCaptor;
	Quest newQuest;
	Quest activeQuest;
	Quest completedQuest;
	Quest expiredQuest;


	static class TestReward extends Reward {
		public TestReward() {
			super();
		}

		@Override
		public void collect() {
			setCollected();
		}
	}

	@BeforeEach
	void beforeEach() {
		ServiceLocator.clear();

		GameArea mockGameArea = mock(GameArea.class);
		when(mockGameArea.getPlayer()).thenReturn(new Entity().addComponent(new InventoryComponent()));
		ServiceLocator.registerGameArea(mockGameArea);

		actorCaptor = ArgumentCaptor.forClass(Actor.class);

		stage = mock(Stage.class);

		RenderService renderService = new RenderService();
		renderService.setStage(stage);

		ServiceLocator.registerRenderService(renderService);
		ServiceLocator.registerTimeSource(new GameTime());
		ServiceLocator.registerTimeService(new TimeService());

		missionManager = new MissionManager();
		ServiceLocator.registerMissionManager(missionManager);

		// new quest
		newQuest = new FertiliseCropTilesQuest("Test Quest", new TestReward(), 10, 10);
		missionManager.addQuest(newQuest);

		// active quest
		activeQuest = new FertiliseCropTilesQuest("Test Quest", new TestReward(), 10, 10);
		missionManager.addQuest(activeQuest);
		missionManager.acceptQuest(activeQuest);

		// completed quest
		completedQuest = new FertiliseCropTilesQuest("Test Quest", new TestReward(), 10, 0);
		missionManager.addQuest(completedQuest);
		missionManager.acceptQuest(completedQuest);

		// expired quest
		expiredQuest = new FertiliseCropTilesQuest("Test Quest", new TestReward(), 1, 1);
		missionManager.addQuest(expiredQuest);
		missionManager.acceptQuest(expiredQuest);
		expiredQuest.updateExpiry();

		Entity questgiver = new Entity();
		missionDisplay = new MissionDisplay();
		questgiver.addComponent(missionDisplay);
		questgiver.create();
	}

	@AfterEach
	void afterEach() {
		reset(stage);
		ServiceLocator.clear();
	}

	/**
	 * Return the button with the desired label from within the table.
	 *
	 * @param table       to search for button in
	 * @param buttonLabel label of the desired button
	 * @return TextButton component
	 */
	private TextButton getButtonFromTable(Table table, String buttonLabel) {
		// get the button
		Cell<?> cell = table.getCells().select(
				c -> c.getActor() instanceof TextButton
						&& ((TextButton) c.getActor()).getLabel().textEquals(buttonLabel)
		).iterator().next();
		return (TextButton) cell.getActor();
	}

	@ParameterizedTest(name = "clicking the {0} button opens the {0} menu, clicking the back button opens the main menu")
	@MethodSource({"clickingButtonGeneratesCorrectMenuParams"})
	void clickingButtonGeneratesCorrectMenuAndReturnsCorrectly(String menuName, int menuButtonIndex) {
		// capture argument passed to stage.addActor when MissionDisplay.openMenu is called
		missionDisplay.openMenu();
		verify(stage).addActor(actorCaptor.capture());

		Actor actor = actorCaptor.getValue();

		// check correct mission window has opened
		assertTrue(actor instanceof Window);
		assertTrue(((Window) actor).getTitleLabel().textEquals("Mission Giver"));
		Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);

		Table buttonTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(buttonTable);

		TextButton nextMenuButton = buttonTable.getChildren().toArray()[menuButtonIndex].firstAscendant(TextButton.class);
		assertNotNull(nextMenuButton);

		// 'click' the next menu button, which should change the window content
		nextMenuButton.toggle();

		assertTrue(((Window) actor).getTitleLabel().textEquals(menuName));

		contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		// get cells that contain a "Back" button
		Cell<?> cell = contentTable.getCells().select(
				c -> c.getActor() instanceof TextButton
						&& ((TextButton) c.getActor()).getLabel().textEquals("< Back")
		).iterator().next();

		TextButton button = (TextButton) cell.getActor();

		button.toggle();

		assertTrue(((Window) actor).getTitleLabel().textEquals("Mission Giver"));
	}

	private static Stream<Arguments> clickingButtonGeneratesCorrectMenuParams() {
		return Stream.of(
				// (menuName, menuButtonIndex)
				arguments("Incomplete Achievements", 0),
				arguments("Active Quests", 1)
		);
	}

	@Test
	void clickingAcceptButtonChangesTheQuestState() {
		missionDisplay.openMenu();
		missionDisplay.openQuests();
		verify(stage).addActor(actorCaptor.capture());

		Actor actor = actorCaptor.getValue();

		// check correct window has opened
		assertTrue(actor instanceof Window);

		// quest ui opens to active quests
		assertTrue(((Window) actor).getTitleLabel().textEquals("Active Quests"));

		// get inner content table, tab table & quest table
		Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);
		Table tabTable = contentTable.getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(tabTable);
		Table questTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(questTable);

		// change tabs to new quests
		TextButton button = getButtonFromTable(tabTable, "New");
		button.toggle();

		// get the "Accept" button
		button = getButtonFromTable(questTable, "Accept");

		// newQuest shouldn't be in the mission manager's active quest list
		assertFalse(missionManager.getActiveQuests().contains(newQuest));

		button.toggle();

		// now it should
		assertTrue(missionManager.getActiveQuests().contains(newQuest));
	}

	@Test
	void clickingReactivateButtonChangesTheQuestState() {
		missionDisplay.openMenu();
		missionDisplay.openQuests();
		verify(stage).addActor(actorCaptor.capture());

		Actor actor = actorCaptor.getValue();

		// check correct window has opened
		assertTrue(actor instanceof Window);

		// quest ui opens to active quests
		assertTrue(((Window) actor).getTitleLabel().textEquals("Active Quests"));

		// get inner content table, tabs table & quest table
		Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);
		Table tabTable = contentTable.getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(tabTable);
		Table questTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(questTable);

		// change tabs to expired quests
		TextButton button = getButtonFromTable(tabTable, "Expired");
		button.toggle();

		// get the "Accept" button
		button = getButtonFromTable(questTable, "Reactivate");

		// quest should be expired
		assertTrue(expiredQuest.isExpired());

		button.toggle();

		// quest should be active again
		assertFalse(expiredQuest.isExpired());
	}

	@Test
	void activeQuestsHaveNoActionButton() {
		missionDisplay.openMenu();
		missionDisplay.openQuests();
		verify(stage).addActor(actorCaptor.capture());

		Actor actor = actorCaptor.getValue();

		// check correct window has opened
		assertTrue(actor instanceof Window);

		// quest ui opens to active quests
		assertTrue(((Window) actor).getTitleLabel().textEquals("Active Quests"));

		// get inner content table, tabs table & quest table
		Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);
		Table tabTable = contentTable.getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(tabTable);
		Table questTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(questTable);

		// change tabs to new quests
		TextButton button = getButtonFromTable(tabTable, "New");
		button.toggle();
		assertTrue(((Window) actor).getTitleLabel().textEquals("New Quests"));

		// change tabs back to active quests
		button = getButtonFromTable(tabTable, "Active");
		button.toggle();
		assertTrue(((Window) actor).getTitleLabel().textEquals("Active Quests"));

		// should only be 3 cells in the table
		//      - quest name
		//      - quest description
		//      - view button
		//      - NO action button
		assertEquals(3, questTable.getCells().size);
	}

	@Test
	void clickingCollectRewardButtonChangesTheRewardState() {
		missionDisplay.openMenu();
		missionDisplay.openQuests();
		verify(stage).addActor(actorCaptor.capture());

		Actor actor = actorCaptor.getValue();

		// check correct window has opened
		assertTrue(actor instanceof Window);

		// quest ui opens to active quests
		assertTrue(((Window) actor).getTitleLabel().textEquals("Active Quests"));

		// get inner content table, tabs table & quest table
		Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);
		Table tabTable = contentTable.getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(tabTable);
		Table questTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(questTable);

		// change tabs to completed quests
		TextButton button = getButtonFromTable(tabTable, "Completed");
		button.toggle();

		// get the "Accept" button
		button = getButtonFromTable(questTable, "Collect Reward");

		// quest should be completed but the reward should not be collected yet
		assertTrue(completedQuest.isCompleted());
		assertFalse(completedQuest.isRewardCollected());

		button.toggle();

		// reward should be collected now
		assertTrue(completedQuest.isRewardCollected());
	}

	@Test
	void clickingViewButtonChangesWindowContent() {
		missionDisplay.openMenu();
		missionDisplay.openQuests();
		verify(stage).addActor(actorCaptor.capture());

		Actor actor = actorCaptor.getValue();

		// check correct window has opened
		assertTrue(actor instanceof Window);

		// quest ui opens to active quests
		assertTrue(((Window) actor).getTitleLabel().textEquals("Active Quests"));

		// get inner content table, tabs table & quest table
		Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);
		Table tabTable = contentTable.getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(tabTable);
		Table questTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(questTable);

		// change tabs to new quests
		TextButton button = getButtonFromTable(tabTable, "New");
		button.toggle();

		assertTrue(((Window) actor).getTitleLabel().textEquals("New Quests"));

		// get the "View" button
		button = getButtonFromTable(questTable, "View");

		// 'click' the button
		button.toggle();

		// window should have changed to show quest info
		assertTrue(((Window) actor).getTitleLabel().textEquals("Quest Info"));

		contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);
		questTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(questTable);

		// should only be 4 cells in the table:
		//      - quest name
		//      - quest description
		//      - accept button
		//      - back button
		assertEquals(4, contentTable.getCells().size);

		// get the "Back" button
		button = getButtonFromTable(questTable, "< Back");

		// 'click' the button
		button.toggle();

		assertTrue(((Window) actor).getTitleLabel().textEquals("Active Quests"));
	}

	@Test
	void clickingAchievementToggleSwitchesView() {
		// complete one of the achievements
		while (!missionManager.getAchievements()[0].isCompleted()) {
			missionManager.getEvents().trigger(MissionManager.MissionEvent.PLANT_CROP.name(), "PlantName");
		}
		// Not the Collect items quest for 10 items is already completed due to previous tests
		missionDisplay.openMenu();
		missionDisplay.openAchievements();
		verify(stage).addActor(actorCaptor.capture());

		Actor actor = actorCaptor.getValue();


		// check correct window has opened
		assertTrue(actor instanceof Window);

		// quest ui opens to active quests
		assertTrue(((Window) actor).getTitleLabel().textEquals("Incomplete Achievements"));

		// get inner content table, tabs table & quest table
		Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(contentTable);
		Table tabTable = contentTable.getChildren().toArray()[0].firstAscendant(Table.class);
		assertNotNull(tabTable);
		Table achievementTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
		assertNotNull(achievementTable);

		int numExpectedAchievements = (int) Arrays.stream(missionManager.getAchievements()).filter(a -> !a.isCompleted()).count();

		// should have 2 cells per achievement in the achievement table:
		//      - 1 x achievement name
		//      - 1 x achievement description
		assertEquals(numExpectedAchievements * 2, achievementTable.getCells().size);

		// switch to the 'Complete' tab
		TextButton button = getButtonFromTable(tabTable, "Complete");

		button.toggle();

		assertTrue(((Window) actor).getTitleLabel().textEquals("Complete Achievements"));


		numExpectedAchievements = (int) Arrays.stream(missionManager.getAchievements()).filter(Mission::isCompleted).count();

		// should have 2 cells per achievement in the achievement table:
		//      - 1 x achievement name
		//      - 1 x achievement description
		assertEquals(numExpectedAchievements * 2, achievementTable.getCells().size);

		// switch back to 'Incomplete' tab
		button = getButtonFromTable(tabTable, "Incomplete");

		button.toggle();

		assertTrue(((Window) actor).getTitleLabel().textEquals("Incomplete Achievements"));
	}
}
