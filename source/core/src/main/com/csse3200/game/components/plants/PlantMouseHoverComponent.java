package com.csse3200.game.components.plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

/**
 * Component for telling the PlantInfoDisplayComponent when the player has their mouse cursor hovering
 * over a plant.
 */
public class PlantMouseHoverComponent extends Component {
    private boolean showInfo;
    private boolean plantDead;
    private boolean noMoreUse;

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        showInfo = false;
        plantDead = false;
        noMoreUse = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        updateInfo();
    }

    /**
     * Used to update the information being shown in the Plant information widget.
     */
    private void updateInfo() {
        if (!plantDead) {
            Vector2 mousePos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            Vector2 tilePos = entity.getComponent(PlantComponent.class).getCropTile().getEntity().getPosition();
            int x = (int) mousePos.x;
            int y = (int) mousePos.y;

            if ((x == tilePos.x) && (y == tilePos.y)) {

                String plantInfo = entity.getComponent(PlantComponent.class).currentInfo();
                String plantName = entity.getComponent(PlantComponent.class).getPlantName();

                ServiceLocator.getPlantInfoService().getEvents().trigger("showPlantInfo", plantName, plantInfo);
                showInfo = true;
            } else {
                if (showInfo) {
                    ServiceLocator.getPlantInfoService().getEvents().trigger("clearPlantInfo");
                    showInfo = false;
                }
            }
        } else {
            if (!noMoreUse) {
                ServiceLocator.getPlantInfoService().getEvents().trigger("clearPlantInfo");
                noMoreUse = true;
            }
        }
    }

    /**
     * Used to signal when the plant has died.
     */
    public void plantDied() {
        this.plantDead = true;
    }
}
