package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class QuestIndicatorComponent extends Component {
    enum State {
        IDLE("empty"),
        QUEST_AVAILABLE("mission_available"),
        REWARD_AVAILABLE("reward_available"),
        OUT_OF_TIME("out_of_time");

        private final String animationName;
        State(String animationName) {
            this.animationName = animationName;
        }
    }

    State state;
    Entity questgiver;
    AnimationRenderComponent parentAnimator;

    /**
     * This will create the questgiver indicator - it listens to events on the mission manager
     * to discover when missions become available, are completed or expire.
     */
    public void create() {
        super.create();
        this.parentAnimator = this.entity.getComponent(AnimationRenderComponent.class);
        updateState(State.QUEST_AVAILABLE);

        MissionManager manager = ServiceLocator.getMissionManager();
        manager.getEvents().addListener("questComplete", this::displayFinishedQuest);
        manager.getEvents().addListener("questComplete", this::displayFinishedQuest);
        manager.getEvents().addListener("questExpired", this::displayExpiredQuest);
        manager.getEvents().addListener("questNew", this::displayNewQuest);
    }

    /**
     * Register the questgiver. This is needed to be able to dismiss the indicator when the player interacts
     * with the questgiver.
     */
    public void registerQuestgiver(Entity questgiver) {
        this.questgiver = questgiver;
        questgiver.getEvents().addListener("toggleMissions", this::markRead);
    }

    /**
     * Display the reward available animation.
     */
    private void displayFinishedQuest() {
        updateState(State.REWARD_AVAILABLE);
    }

    /**
     * Display the quest expired animation.
     */
    private void displayExpiredQuest() {
        updateState(State.OUT_OF_TIME);
    }

    /**
     * Display the new quest animation.
     */
    private void displayNewQuest() {
        updateState(State.QUEST_AVAILABLE);
    }

    /**
     * Mark as read - hide any animation.
     */
    private void markRead() {
        updateState(State.IDLE);
    }

    /**
     * Show a given animation. 
     * @param newState New animation state
     */
    private void updateState(State newState) {
        this.state = newState;
        parentAnimator.startAnimation(newState.animationName);
    }
}
