package com.csse3200.game.components.placeables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.missions.cutscenes.Cutscene;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class DialogueComponent extends UIComponent {

    private final static String dialogue = """
            Hey pookie, how are you?
            Hmm, not much of a talker huh?
            You look lost.
            I can fix that...
            Talk to me again and I can take you away from here.
            """;

    private boolean talked;
    private Entity cutscene;
    private boolean waitingForCredits;
    Image transparentRectangle;

    @Override
    public void draw(SpriteBatch batch) {
        // do nothing
    }

    @Override
    public void create() {
        super.create();
        talked = false;
        waitingForCredits = false;

        // setup for dimming screen, taken from Team 3's cutscene dimming.
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture transparentRecTex = new Texture(pixmap);
        pixmap.dispose();

        transparentRectangle = new Image(transparentRecTex);
        transparentRectangle.setVisible(false);
        transparentRectangle.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(transparentRectangle);

        entity.getEvents().addListener("interact", this::interact);
        entity.getEvents().addListener("dim", this::dim);
        entity.getEvents().addListener("dimFinished", this::win);
    }

    private void win() {
        ServiceLocator.getTimeService().setPaused(true);
        ServiceLocator.getMissionManager().getEvents().trigger("creditScreen");
    }

    private void dim(float alpha) {
        if (alpha >= 1.0f) {
            entity.getEvents().trigger("dimFinished");
            return;
        }
        transparentRectangle.getColor().a = alpha;
        transparentRectangle.setVisible(true);
        entity.getEvents().scheduleEvent(0.05f, "dim", alpha + 0.05f);
    }

    @Override
    public void update() {
        cutscene.setCenterPosition(entity.getCenterPosition());
        if (waitingForCredits && cutscene.getComponent(AnimationRenderComponent.class).isFinished()) {
            waitingForCredits = false;
            cutscene.getComponent(AnimationRenderComponent.class).stopAnimation();

            this.entity.getEvents().trigger("dim", 0f);
        }
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

        ServiceLocator.getGameArea().getPlayer().getEvents().trigger("hideUI");
        ServiceLocator.getUIService().getEvents().trigger("toggleUI", false);
        ServiceLocator.getPlantInfoService().getEvents().trigger("toggleOpen", false);

        ServiceLocator.getCameraComponent().setTrackEntity(entity);

        cutscene.getComponent(AnimationRenderComponent.class).startAnimation("default");
        entity.getComponent(AnimationRenderComponent.class).stopAnimation();
        waitingForCredits = true;
    }

    private void talk() {
        Cutscene cutscene = new Cutscene(dialogue, Cutscene.CutsceneType.PLACEABLE);
        cutscene.spawnCutscene();
        talked = true;
    }

    public void addCutsceneAnimation(Entity cutscene) {
        this.cutscene = cutscene;
        this.cutscene.setCenterPosition(entity.getCenterPosition());
        ServiceLocator.getGameArea().spawnEntity(this.cutscene);
    }

    @Override
    public void dispose() {
        stage.getRoot().removeActor(transparentRectangle);
        super.dispose();
    }
}
