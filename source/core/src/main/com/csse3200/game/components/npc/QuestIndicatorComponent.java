package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class QuestIndicatorComponent extends Component {
    enum State {
        IDLE("empty"),
        QUEST_AVAILABLE("quest_available"),
        REWARD_AVAILABLE("reward_available"),
        OUT_OF_TIME("out_of_time");

        private final String animationName;
        State(String animationName) {
            this.animationName = animationName;
        }
    }

    State state;
    AnimationRenderComponent parentAnimator;

    public QuestIndicatorComponent(Entity questGiver) {
        super.create();
        this.parentAnimator = this.entity.getComponent(AnimationRenderComponent.class);
        this.entity.getEvents().addListener("newState", this::updateState);
        updateState(State.IDLE);
    }

    public void updateState(State newState) {
        this.state = newState;
        parentAnimator.startAnimation(newState.animationName);
    }

    public void updateLocation(Vector2 parentPos) {
        this.entity.setPosition(parentPos.x, parentPos.y - 32);
    }
}
