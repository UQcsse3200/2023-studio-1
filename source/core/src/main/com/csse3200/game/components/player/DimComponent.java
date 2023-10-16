package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.ui.UIComponent;

public class DimComponent extends UIComponent {

    Image transparentRectangle;

    @Override
    public void create() {
        super.create();

        // setup for dimming screen, taken from Team 3's cutscene dimming.
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture transparentRecTex = new Texture(pixmap);
        pixmap.dispose();

        transparentRectangle = new Image(transparentRecTex);
        transparentRectangle.setVisible(false);
        transparentRectangle.setColor(Color.BLACK);
        transparentRectangle.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(transparentRectangle);

        entity.getEvents().addListener("blackOut", this::visible);
        entity.getEvents().addListener("visibleFalse", this::invis);
    }

    private void invis() {
        transparentRectangle.setVisible(false);
    }

    private void visible() {
        transparentRectangle.setVisible(true);
        entity.getEvents().scheduleEvent(0.15f, "visibleFalse");
    }


    @Override
    protected void draw(SpriteBatch batch) {
        // Do not think i will
    }
}
