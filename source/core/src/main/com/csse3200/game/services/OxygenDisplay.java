package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;

import java.security.Provider;

/**
 * A ui component for displaying the current oxygen level on the Main Game Screen.
 */
public class OxygenDisplay extends UIComponent{

    Table table = new Table();
    Group group = new Group();
    private Image oxygenOutline;
    private Image oxygenFill;
    private Array<Label> oxygenLabels;
    private Label oxygenLabel;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();

        // Adds a listener to check for oxygen updates
        ServiceLocator.getPlanetOxygenService().getEvents()
                .addListener("oxygenUpdate", this::updateDisplay);

        updateDisplay();
    }

    /**
     * Initialises all the possible images and labels that will be used by
     * the class, and stores them in an array to be called when needed.
     */
    public void createTexture() {
        Skin oxygenSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        oxygenOutline = new Image(ServiceLocator.getResourceService().getAsset(
            "images/oxygen_ui/oxygen_outline.png", Texture.class));
        oxygenFill = new Image(ServiceLocator.getResourceService().getAsset(
            "images/oxygen_ui/oxygen_fill.png", Texture.class));

        oxygenLabels = new Array<>();
        for (int i = 0; i <= 100; i++) {
            oxygenLabels.add(new Label(String.format("Oxygen: %d%%", i), oxygenSkin));
        }
    }

    /**
     * Updates the display, showing the oxygen bar in the top of the main game screen.
     */
    public void updateDisplay() {
        if (oxygenLabels == null) {
            createTexture();
        }

        int oxygenPercent = ServiceLocator.getPlanetOxygenService().getOxygenPercentage();
        float scaling = (float) oxygenPercent / 100;

        if (oxygenPercent == 0) {
            ServiceLocator.getGameArea().getPlayer().getEvents().trigger("loseScreen");
        }

        // Accounts for scaling of the oxygen bar due to the oxygen percent
        oxygenFill.setX(oxygenFill.getImageX() + 14 * (1 - scaling));
        oxygenFill.setScaleX(scaling);

        // Add a safety check to ensure that the array is always accessed at a possible index
        if (0 <= oxygenPercent && oxygenPercent <= 100) {
            oxygenLabel = oxygenLabels.get(oxygenPercent);
            oxygenLabel.setPosition(oxygenOutline.getImageX() + 125f, oxygenOutline.getImageY() + 8.5f);
        }

        // Uncomment line below to test that oxygen percent increases by 1% per hour.
        //ServiceLocator.getPlanetOxygenService().addOxygen(10);
    }

    /**
     * Draws the table and group onto the main game screen. Adds the oxygen elements onto the stage.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        table.clear();
        group.clear();

        table.top();
        table.setFillParent(true);
        table.padTop(-130f).padLeft(-180f);

        group.addActor(oxygenOutline);
        group.addActor(oxygenFill);
        group.addActor(oxygenLabel);

        table.add(group).size(200);
        stage.addActor(table);
    }

    /**
     * Removes all entities from the screen. Releases all resources from this class.
     */
    @Override
    public void dispose() {
        super.dispose();
        oxygenOutline.remove();
        oxygenFill.remove();
        oxygenLabel.remove();
    }
}
