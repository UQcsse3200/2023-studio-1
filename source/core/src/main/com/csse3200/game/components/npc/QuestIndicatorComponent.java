package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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

    public void registerQuestgiver(Entity questgiver) {
        this.questgiver = questgiver;
        questgiver.getEvents().addListener("toggleMissions", this::markRead);
    }

    private void displayFinishedQuest() {
        updateState(State.REWARD_AVAILABLE);
    }

    private void displayExpiredQuest() {
        updateState(State.OUT_OF_TIME);
    }

    private void displayNewQuest() {
        updateState(State.QUEST_AVAILABLE);
    }

    private void markRead() {
        updateState(State.IDLE);
    }

    public void updateState(State newState) {
        this.state = newState;
        parentAnimator.startAnimation(newState.animationName);
    }
}
