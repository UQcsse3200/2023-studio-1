package com.csse3200.game.components.placeables;

import java.util.HashMap;

import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;

public class FenceComponent extends Component {

    private boolean isGate = false;
    private boolean isOpen = false;

    /* Current texture of the entity */
    private DynamicTextureRenderComponent currentTexture;
    
    private String texturePath;

    /**
     * The paths to the sound associated with the gate
     */
    private String[] sounds;

    private String closedGatePath = "images/fences/f.png";
    private String openGatePath = "images/fences/f_l.png";
    private String fenceBase = "";

    public FenceComponent(boolean isGate) {
        this.isGate = isGate;
    }

    public FenceComponent() {
    }

    /**
     * {@inheritDoc}
    */
    @Override
    public void create() {
        super.create();

        this.currentTexture = entity.getComponent(DynamicTextureRenderComponent.class);

        // Initialise event listener for gate.
        if (isGate) {
            entity.getEvents().addListener("interact", this::toggleGate);
            texturePath = closedGatePath;
        } else {
            texturePath = fenceBase;
        }

        /* Check if we placed next to another gate and update accordingly */
        this.updateTexture();

    }

    private void toggleGate() {
        isOpen = !isOpen;

        if (isOpen) {
            this.currentTexture.setTexture(openGatePath);
            this.entity.getComponent(ColliderComponent.class).setSensor(true);
            return;
        }
        this.entity.getComponent(ColliderComponent.class).setSensor(false);
        this.currentTexture.setTexture(closedGatePath);
    }

    private void updateTexturePath() {

    }

    public void updateTexture() {
        this.currentTexture.setTexture(this.texturePath);
    }

}