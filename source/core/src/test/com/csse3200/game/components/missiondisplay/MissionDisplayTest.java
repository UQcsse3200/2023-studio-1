package com.csse3200.game.components.missiondisplay;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.questgiver.MissionDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.FertiliseCropTilesQuest;
import com.csse3200.game.missions.rewards.ItemReward;
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

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class MissionDisplayTest {
    Stage stage;
    MissionManager missionManager;
    MissionDisplay missionDisplay;
    ArgumentCaptor<Actor> actorCaptor;

    @BeforeEach
    void beforeEach() {
        actorCaptor = ArgumentCaptor.forClass(Actor.class);

        stage = mock(Stage.class);

        RenderService renderService = new RenderService();
        renderService.setStage(stage);

        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());

        missionManager = new MissionManager();
        ServiceLocator.registerMissionManager(missionManager);

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

    @ParameterizedTest(name = "clicking the {0} button opens the {0} menu, clicking the back button opens the main menu")
    @MethodSource({"clickingButtonGeneratesCorrectMenuParams"})
    void clickingButtonGeneratesCorrectMenuAndReturnsCorrectly(String menuName, int menuButtonIndex) {
        // capture argument passed to stage.addActor when MissionDisplay.openMenu is called
        missionDisplay.openMenu();
        verify(stage).addActor(actorCaptor.capture());

        Actor actor = actorCaptor.getValue();

        // check correct mission window has opened
        assert(actor instanceof Window);
        assert(((Window) actor).getTitleLabel().textEquals("Mission Giver"));
        Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        Table buttonTable = contentTable.getChildren().toArray()[1].firstAscendant(Table.class);
        assert(buttonTable != null);

        TextButton nextMenuButton = buttonTable.getChildren().toArray()[menuButtonIndex].firstAscendant(TextButton.class);
        assert(nextMenuButton != null);

        // 'click' the next menu button, which should change the window content
        nextMenuButton.toggle();

        assert(((Window) actor).getTitleLabel().textEquals(menuName));

        contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        // get cells that contain a "Back" button
        Cell<?> cell = contentTable.getCells().select(
                c -> c.getActor() instanceof TextButton
                        && ((TextButton) c.getActor()).getLabel().textEquals("< Back")
        ).iterator().next();

        TextButton button = (TextButton) cell.getActor();

        button.toggle();

        assert(((Window) actor).getTitleLabel().textEquals("Mission Giver"));
    }

    private static Stream<Arguments> clickingButtonGeneratesCorrectMenuParams() {
        return Stream.of(
                // (menuName, menuButtonIndex)
                arguments("Achievements", 0),
                arguments("Quests", 1)
        );
    }

    @Test
    void clickingAcceptButtonChangesTheQuestState() {
        missionDisplay.openMenu();
        missionDisplay.openQuests();
        verify(stage).addActor(actorCaptor.capture());

        Actor actor = actorCaptor.getValue();

        // check correct window has opened
        assert(actor instanceof Window);
        assert(((Window) actor).getTitleLabel().textEquals("Quests"));
        Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        // get cells that contain an "Accept" button
        Cell<?> cell = contentTable.getCells().select(
                c -> c.getActor() instanceof TextButton
                        && ((TextButton) c.getActor()).getLabel().textEquals("Accept")
        ).iterator().next();

        TextButton button = (TextButton) cell.getActor();

        // there should be no active quests before 'clicking' "Accept"
        assert(missionManager.getActiveQuests().isEmpty());

        button.toggle();

        // there should now be an active quest
        assert(!missionManager.getActiveQuests().isEmpty());
    }
    @Test
    void clickingReactivateButtonChangesTheQuestState() {
        FertiliseCropTilesQuest testQuest = new FertiliseCropTilesQuest("Test Quest", new ItemReward(new ArrayList<>()), 1, 1);
        missionManager.addQuest(testQuest);
        missionManager.acceptQuest(testQuest);

        // expire the quest
        testQuest.updateExpiry();

        missionDisplay.openMenu();
        missionDisplay.openQuests();
        verify(stage).addActor(actorCaptor.capture());

        Actor actor = actorCaptor.getValue();

        // check correct window has opened
        assert(actor instanceof Window);
        assert(((Window) actor).getTitleLabel().textEquals("Quests"));
        Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        // get cells that contain a "Reactivate" button
        Cell<?> cell = contentTable.getCells().select(
                c -> c.getActor() instanceof TextButton
                        && ((TextButton) c.getActor()).getLabel().textEquals("Reactivate")
        ).iterator().next();

        TextButton button = (TextButton) cell.getActor();

        // quest should be expired
        assert(testQuest.isExpired());

        button.toggle();

        // quest should be active again
        assert(!testQuest.isExpired());
    }

    @Test
    void clickingCollectRewardButtonChangesTheRewardState() {
        class TestReward extends Reward {
            public TestReward() {
                super();
            }

            @Override
            public void collect() {
                setCollected();
            }
        }

        TestReward testReward = new TestReward();

        FertiliseCropTilesQuest testQuest = new FertiliseCropTilesQuest("Test Quest", testReward, 1, 1);
        missionManager.addQuest(testQuest);
        missionManager.acceptQuest(testQuest);

        // update the quest state
        missionManager.getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());

        missionDisplay.openMenu();
        missionDisplay.openQuests();
        verify(stage).addActor(actorCaptor.capture());

        Actor actor = actorCaptor.getValue();

        // check correct window has opened
        assert(actor instanceof Window);
        assert(((Window) actor).getTitleLabel().textEquals("Quests"));
        Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        // get cells that contain a "Collect Reward" button
        Cell<?> cell = contentTable.getCells().select(
                c -> c.getActor() instanceof TextButton
                        && ((TextButton) c.getActor()).getLabel().textEquals("Collect Reward")
        ).iterator().next();

        TextButton button = (TextButton) cell.getActor();

        // quest should be completed but the reward should not be collected yet
        assert(testQuest.isCompleted());
        assert(!testReward.isCollected());

        button.toggle();

        // reward should be collected now
        assert(testReward.isCollected());
    }

    @Test
    void clickingViewButtonChangesWindowContent() {
        missionDisplay.openMenu();
        missionDisplay.openQuests();
        verify(stage).addActor(actorCaptor.capture());

        Actor actor = actorCaptor.getValue();

        // check correct window has opened
        assert(actor instanceof Window);
        assert(((Window) actor).getTitleLabel().textEquals("Quests"));
        Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        // get cells that contain a "View" button
        Cell<?> cell = contentTable.getCells().select(
                c -> c.getActor() instanceof TextButton
                        && ((TextButton) c.getActor()).getLabel().textEquals("View")
        ).iterator().next();

        TextButton button = (TextButton) cell.getActor();

        button.toggle();

        contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        // should only be 4 cells in the table:
        //      - quest name
        //      - quest description
        //      - accept button
        //      - back button
        assert(contentTable.getCells().size == 4);
    }

    @Test
    void clickingAchievementToggleSwitchesView() {
        // complete one of the achievements
        while (!missionManager.getAchievements()[0].isCompleted()) {
            missionManager.getEvents().trigger(MissionManager.MissionEvent.PLANT_CROP.name(), "PlantName");
        }

        missionDisplay.openMenu();
        missionDisplay.openAchievements();
        verify(stage).addActor(actorCaptor.capture());

        Actor actor = actorCaptor.getValue();

        // check correct window has opened
        assert(actor instanceof Window);
        assert(((Window) actor).getTitleLabel().textEquals("Achievements"));
        Table contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        // find the achievements table cell
        Cell<?> cell = contentTable.getCells().select(
                c -> c.getActor() instanceof Table
        ).iterator().next();

        Table achievementTable = (Table) cell.getActor();

        // should have 4 cells in the achievement table:
        //      - 2 x achievement names
        //      - 2 x achievement descriptions
        assert(achievementTable.getCells().size == 4);

        // get cells that contain a "Show Completed" button
        cell = contentTable.getCells().select(
                c -> c.getActor() instanceof TextButton
                        && ((TextButton) c.getActor()).getLabel().textEquals("Show Completed")
        ).iterator().next();

        TextButton button = (TextButton) cell.getActor();

        button.toggle();
        // find content & achievement tables again
        contentTable = ((Window) actor).getChildren().toArray()[0].firstAscendant(Table.class);
        assert(contentTable != null);

        cell = contentTable.getCells().select(
                c -> c.getActor() instanceof Table
        ).iterator().next();

        achievementTable = (Table) cell.getActor();

        // should only have 2 cells in the achievement table now:
        //      - 1 x achievement name
        //      - 1 x achievement description
        assert(achievementTable.getCells().size == 2);
    }
}
