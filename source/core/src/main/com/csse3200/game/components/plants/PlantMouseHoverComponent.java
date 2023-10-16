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
    /**
     * Indicates whether plant information is currently being shown.
     */
    private boolean showInfo;
    /**
     * Indicates whether the associated plant has died.
     */
    private boolean plantDead;
    /**
     * Indicates whether the plant information should no longer be used.
     */
    private boolean noMoreUse;

    /**
     * Used to signal when the plant has died.
     */
    public void setPlantDied(boolean plantDied) {
        this.plantDead = plantDied;
    }

    /**
     * Checks if the plant is dead.
     * @return true if the plant is dead, false otherwise.
     */
    public boolean isPlantDead() {
        return this.plantDead;
    }

    /**
     * Checks whether plant information is currently being shown.
     * @return {@code true} if plant information is being shown, {@code false} otherwise.
     */
    public boolean isShowInfo() {
        return showInfo;
    }

    /**
     * Sets whether plant information is currently being shown.
     * @param showInfo {@code true} to indicate that plant information is being shown, {@code false} otherwise.
     */
    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
    }

    /**
     * Checks whether the plant information should no longer be used.
     * @return {@code true} if the plant information should no longer be used, {@code false} otherwise.
     */
    public boolean isNoMoreUse() {
        return noMoreUse;
    }

    /**
     * Sets whether the plant information should no longer be used.
     * @param noMoreUse {@code true} to indicate that the plant information should no longer be used, {@code false} otherwise.
     */
    public void setNoMoreUse(boolean noMoreUse) {
        this.noMoreUse = noMoreUse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        setShowInfo(false);
        setPlantDied(false);
        setNoMoreUse(false);
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
    public void updateInfo() {
        if (!isPlantDead()) {
            Vector2 mousePos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            Vector2 tilePos = entity.getComponent(PlantComponent.class).getCropTile().getEntity().getPosition();
            int x = (int) mousePos.x;
            int y = (int) mousePos.y;

            if ((x == tilePos.x) && (y == tilePos.y)) {

                String plantInfo = entity.getComponent(PlantComponent.class).currentInfo();
                String plantName = entity.getComponent(PlantComponent.class).getPlantName();

                ServiceLocator.getPlantInfoService().getEvents().trigger("showPlantInfo", plantName, plantInfo);
                setShowInfo(true);
            } else {
                if (isShowInfo()) {
                    ServiceLocator.getPlantInfoService().getEvents().trigger("clearPlantInfo");
                    setShowInfo(false);
                }
            }
        } else {
            if (!isNoMoreUse()) {
                ServiceLocator.getPlantInfoService().getEvents().trigger("clearPlantInfo");
                setNoMoreUse(true);
            }
        }
    }
}
