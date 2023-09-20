package com.csse3200.game.components.plants;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.plants.PlantInfoService;
import com.csse3200.game.ui.UIComponent;

public class PlantInfoDisplayComponent extends UIComponent {

    private Window window;
    private Label label;

    @Override
    public void create() {
        super.create();
        addActors();
        ServiceLocator.getPlantInfoService().getEvents().addListener("testPlant", this::testPlant);
    }

    private void addActors() {
        window = new Window("Plant information", skin);
        window.setVisible(true);
        window.setSize(1000f, 250f);
        window.padBottom(10f);
        window.setPosition(0f, 20f);
        window.setMovable(false);
        /*
        label = new Label("Ello guvna", skin);
        label.setFontScale(2f);
        label.setColor(Color.BLACK);
        window.add(label);
        stage.addActor(window);
         */
        stage.addActor(window);
    }

    public void testPlant(String test) {
        label = new Label(test, skin);
        label.setFontScale(2f);
        label.setColor(Color.BLACK);
        window.add(label);
        stage.addActor(window);
    }



    @Override
    protected void draw(SpriteBatch batch) {

    }
}
