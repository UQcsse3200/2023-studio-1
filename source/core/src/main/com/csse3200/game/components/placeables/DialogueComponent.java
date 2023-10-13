package com.csse3200.game.components.placeables;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.missions.cutscenes.Cutscene;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class DialogueComponent extends Component {

    private final static String dialogue = """
            Hey pookie, how are you?
            Hmm, not much of a talker huh?
            You look lost.
            I can fix that...
            Talk to me again and I can take you away from here.
            """;

    private boolean talked;
    private Entity cutscene;

    @Override
    public void create() {
        entity.getEvents().addListener("interact", this::interact);
        entity.getEvents().addListener("DjKhaledWin", this::win);
    }

    private void win() {
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.ENDCREDITS);
    }

    @Override
    public void update() {
        cutscene.setCenterPosition(entity.getCenterPosition());
    }

    private void interact() {
        if (talked) {
            fly();
            return;
        }
        talk();
    }

    private void fly() {
        // Fly away with DJ Khaled
        ServiceLocator.getGameArea().getPlayer().setPosition(-10, -10);
        ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class).setMuted(true);
        ServiceLocator.getCameraComponent().setTrackEntity(entity);
        cutscene.getComponent(AnimationRenderComponent.class).startAnimation("cast_left");
        entity.getComponent(AnimationRenderComponent.class).stopAnimation();
        entity.getEvents().scheduleEvent(1,"DjKhaledWin");
        //entity.getComponent(AnimationRenderComponent.class).startAnimation("invis");
    }

    private void talk() {
        Cutscene cutscene = new Cutscene(dialogue, Cutscene.CutsceneType.PLACEABLE);
        cutscene.spawnCutscene();
        talked = true;
    }

    public void addCutsceneAnimation(Entity cutscene) {
        this.cutscene = cutscene;
    }
}
