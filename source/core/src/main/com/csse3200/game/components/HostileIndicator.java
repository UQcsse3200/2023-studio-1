package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.components.*;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.entities.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.rendering.RenderService;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.graphics.GL20;

public class HostileIndicator extends UIComponent{

    private Image indicator;
    private Entity entity;
    private float indicatorPosX;
    private float indicatorPosY;
    private CameraComponent cameraComponent;
    private final float indicatorOffset = 10;

    public HostileIndicator(Entity entity) {
        this.entity = entity;
        cameraComponent = ServiceLocator.getCameraComponent();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        indicator = new Image(ServiceLocator.getResourceService().getAsset("images/hostile_indicator.png", Texture.class));
        stage.addActor(indicator);
    }

    @Override
    public void update() {
        if (cameraComponent.entityOnScreen(entity)) {
            indicator.setVisible(false);
        } else {
            indicator.setVisible(true);
            updateIndicator();
        }
    }

    public void updateIndicator() {
        Vector2 entityPosition = entity.getCenterPosition();
        Vector3 entityPos = new Vector3(entityPosition.x, entityPosition.y, 0);
        cameraComponent.getCamera().project(entityPos);

        indicatorPosX = MathUtils.clamp(entityPos.x - indicatorOffset, indicatorOffset,
                Gdx.graphics.getWidth() - indicator.getWidth() - indicatorOffset);
        indicatorPosY = MathUtils.clamp(entityPos.y - indicatorOffset, indicatorOffset,
                Gdx.graphics.getHeight() - indicator.getHeight() - indicatorOffset);

        indicator.setPosition(indicatorPosX, indicatorPosY);
    }

    @Override
    public void draw(SpriteBatch batch) {
    }

    @Override
    public void dispose() {
        indicator.clear();
        super.dispose();
    }
}