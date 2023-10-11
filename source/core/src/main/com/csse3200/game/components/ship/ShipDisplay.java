package com.csse3200.game.components.ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.components.ship.ShipTimeSkipComponent;
import com.csse3200.game.components.ship.ShipLightComponent;
public class ShipDisplay extends UIComponent {
    private Window window;
    private boolean isOpen;
    private boolean isLightOn = false;

    @Override
    public void create() {
        super.create();
        isOpen = false;
        addActors();

        entity.getEvents().addListener("interact", this::toggleOpen);
    }

    private void addActors() {
        window = new Window("Ship Control", skin);
        window.setVisible(false);
        stage.addActor(window);
    }

    private void updateWindow() {
        window.setMovable(false);
        window.pad(50, 10, 10, 10);
        window.pack();
        window.setWidth(800f);
        window.setPosition(
                stage.getWidth() / 2 - window.getWidth() / 2,
                stage.getHeight() / 2 - window.getHeight() / 2
        );
    }

    private void generateShipControls() {
        window.clear();
        window.getTitleLabel().setText("Ship Controls");

        TextButton toggleLightButton = new TextButton(isLightOn ? "Turn Light Off" : "Turn Light On", skin);
        toggleLightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {

                entity.getEvents().trigger("toggleLight");

            }
        });

        TextButton timeSkipButton = new TextButton("Sleep (Time Skip)", skin);
        timeSkipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                entity.getEvents().trigger("timeSkip");
            }
        });

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                toggleOpen();
            }
        });

        Table contentTable = new Table();
        contentTable.defaults().size(500f, 50f).padBottom(10f);
        contentTable.row().pad(30f);
        contentTable.add(toggleLightButton).fillX();
        contentTable.row().pad(30f);
        contentTable.add(timeSkipButton).fillX();
        contentTable.row().pad(30f);
        contentTable.add(closeButton).fillX();

        window.add(contentTable).expand().fill();
        updateWindow();
    }

    @Override
    public void draw(SpriteBatch batch) {
    }

    public void toggleOpen() {
        if (isOpen) {
            window.setVisible(false);
            isOpen = false;
        } else {
            generateShipControls();
            window.setVisible(true);
            isOpen = true;
        }
    }

    @Override
    public void dispose() {
        window.clear();
        super.dispose();
    }
}
