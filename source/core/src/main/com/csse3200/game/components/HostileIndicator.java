package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.*;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.entities.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.rendering.RenderService;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.utils.Array;

public class HostileIndicator extends UIComponent{

    Table table;
    private Label indicator;

    private Entity entity;
    private CameraComponent cameraComponent;

    public HostileIndicator(Entity entity) {
        this.entity = entity;
        cameraComponent = ServiceLocator.getCameraComponent();
        cameraComponent.setTrackEntity(this.entity);
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        indicator = new Label("HERE!!!", skin, "large");

        table = new Table();
        table.setFillParent(true);
        table.add(indicator);
        stage.addActor(table);
    }

    public void updateDisplay() {
        table.setVisible(true);
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {
        table.clear();
        indicator.clear();
        super.dispose();
    }
}