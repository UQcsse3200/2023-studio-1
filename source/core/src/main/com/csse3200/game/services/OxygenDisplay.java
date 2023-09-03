package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying the current oxygen level on the Main Game Screen.
 */
public class OxygenDisplay extends UIComponent{

    Table table;
    Group group;
    private Image oxygenOutline;
    private Image oxygenFill;
    private Label oxygenLabel;
    private Skin oxygenSkin;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        oxygenSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        updateDisplay();
    }

    /**
     * Updates the display, showing the oxygen bar in the top of the main game screen.
     */
    public void updateDisplay() {
        int oxygenPercent = 50;
        float scaling = (float) oxygenPercent / 100;
        table = new Table();
        group = new Group();
        table.top();
        table.setFillParent(true);
        table.padTop(-130f).padLeft(-180f);

        oxygenOutline = new Image(ServiceLocator.getResourceService().getAsset(
            "images/oxygen_ui/oxygen_outline.png", Texture.class));
        oxygenFill = new Image(ServiceLocator.getResourceService().getAsset(
            "images/oxygen_ui/oxygen_fill.png", Texture.class));

        // Accounts for scaling of the oxygen bar due to the oxygen percent
        oxygenFill.setX(oxygenFill.getImageX() + 14 * (1 - scaling));
        oxygenFill.setScaleX(scaling);

        oxygenLabel = new Label(String.format("Oxygen: %d%%", oxygenPercent), oxygenSkin);
        oxygenLabel.setPosition(oxygenOutline.getImageX() + 125f, oxygenOutline.getImageY() + 8.5f);

        group.addActor(oxygenOutline);
        group.addActor(oxygenFill);
        group.addActor(oxygenLabel);

        table.add(group).size(200);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Does nothing since draw
        return;
    }


    @Override
    public void dispose() {
        super.dispose();
        oxygenOutline.remove();
        oxygenFill.remove();
        oxygenLabel.remove();
    }
}
