package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ItemFrame extends Actor {
    private final ShapeRenderer shapeRenderer;
    private boolean selected;

    public ItemFrame(boolean selected) {
        shapeRenderer = new ShapeRenderer();
        this.selected = selected;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        if (selected) {
            Gdx.gl.glLineWidth(4f);
            shapeRenderer.setColor(new Color(0x76428aff));
        }
        else {
            Gdx.gl.glLineWidth(2f);
            shapeRenderer.setColor(Color.BLACK);
        }

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.rect(getX(), getY(), 67, 67);

        shapeRenderer.end();

        batch.begin();
    }

    public void updateSelected(boolean selected) {
        this.selected = selected;
    }
}
