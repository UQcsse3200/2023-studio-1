package com.csse3200.game.components.plants;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.plants.PlantInfoService;
import com.csse3200.game.ui.UIComponent;

public class PlantInfoDisplayComponent extends UIComponent {

    private Window window;
    private Label label;
    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
        ServiceLocator.getPlantInfoService().getEvents().addListener("testPlant", this::testPlant);
        //ServiceLocator.getPlantInfoService().getEvents().addListener("clearPlantInfo", this::clearInfo);
        createWindow();
        label = new Label("Hover your mouse over a plant", skin);
        label.setFontScale(1.5f);
        label.setColor(Color.BROWN);
        window.add(label);
        stage.addActor(window);

    }

    private void addActors() {
        createWindow();
    }

    private void createWindow() {
        window = new Window("Plant information", skin);
        window.setVisible(true);
        window.setSize(700f, 300f);
        window.padBottom(10f);
        window.setPosition(20f, 20f);
        window.setMovable(false);

        stage.addActor(window);
    }

    public void testPlant(String test) {
        window.reset();
        createWindow();



        label = new Label(test, skin);
        label.setFontScale(1.5f);
        label.setColor(Color.BROWN);

        window.add(label);
        stage.addActor(window);
    }





    @Override
    protected void draw(SpriteBatch batch) {

    }
}
