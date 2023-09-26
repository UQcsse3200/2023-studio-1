package com.csse3200.game.components.plants;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class PlantInfoDisplayComponent extends UIComponent {

    /**
     * The window used to display the plant information.
     */
    private Window window;

    /**
     * The label for the text displaying the plant information.
     */
    private Label label;

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        super.create();
        ServiceLocator.getPlantInfoService().getEvents().addListener("showPlantInfo", this::showPlantInfo);
        ServiceLocator.getPlantInfoService().getEvents().addListener("clearPlantInfo", this::clearInfo);
        createWindow();
        label = new Label("Hover your mouse \nover a plant", skin);
        label.setFontScale(1.5f);
        label.setColor(Color.BROWN);
        window.add(label);
        stage.addActor(window);

    }

    /**
     * Create the window used to display the plant information.
     */
    private void createWindow() {
        window = new Window("Plant information", skin);
        window.setVisible(true);
        window.setSize(450f, 270f);
        window.padBottom(10f);
        window.setPosition(20f, 20f);
        window.setMovable(false);
        stage.addActor(window);
    }

    /**
     * Display the string of plant information in the window. This information is
     * provided by the plant component and is already formatted. This occurs when the
     * player hovers their mouse cursor over a plant.
     * @param plantInfo - Information about the current state of the plant.
     */
    public void showPlantInfo(String plantInfo) {
        window.reset();
        createWindow();
        label = new Label(plantInfo, skin);
        label.setFontScale(1.5f);
        label.setColor(Color.BROWN);
        window.add(label);
        stage.addActor(window);
    }

    /**
     * Clears the window of any plant information.
     */
    public void clearInfo() {
        createWindow();
        String info = ServiceLocator.getPlantInfoService().plantInfoSummary();
        label = new Label(info, skin);
        label.setFontScale(1.5f);
        label.setColor(Color.BROWN);
        window.add(label);
        stage.addActor(window);
    }

    /**
     * Has no use in this component but is required by the UIComponent class.
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // No use but required.
    }
}
